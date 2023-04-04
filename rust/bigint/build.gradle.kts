import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import tech.antibytes.gradle.configuration.sourcesets.iosx
import tech.antibytes.gradle.configuration.sourcesets.setupAndroidTest
import tech.antibytes.gradle.dependency.helper.nodePackage

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    id(antibytesCatalog.plugins.kotlin.parcelize.get().pluginId)

    alias(libs.plugins.rust)
    alias(libs.plugins.kmock)
}

val rustDir = "${projectDir.absolutePath.trimEnd('/')}/rsrc"
val jvmRustLib = "$buildDir/generated/rust/jvm"
val wasmRustLib = "$projectDir/wasm"
val platform = "osx_arm64" // TODO

val userHome = project.findProperty("gradle.user.home") as String?

cargo {
    prebuiltToolchains = true
    module = rustDir
    libname = "bigint_arithmetic"
    targets = listOf(
        "arm",
        "arm64",
        "x86",
        "x86_64",
    )
    targetIncludes = arrayOf("*.*")
    pythonCommand = "python3"

    if (userHome != null) {
        cargoCommand = "$userHome/.cargo/bin/cargo"
        rustcCommand = "$userHome/.cargo/bin/rustc"
    }
}

val rustJvmAssemble by tasks.creating(Exec::class.java) {
    workingDir = File(rustDir)
    val cargo = if (userHome != null) {
        "$userHome/.cargo/bin/cargo"
    } else {
        "cargo"
    }

    commandLine(cargo, "build", "--release", "--features", "jvm", "--no-default-features")
}
val rustJvmBuild by tasks.creating(Sync::class.java) {
    group = "Rust"
    dependsOn(rustJvmAssemble)

    from("$rustDir/target/release")
    include("*.dylib", "*.so", "*.dll")
    into("$jvmRustLib/natives/$platform")
}

val rustWasmAssemble by tasks.creating(Exec::class.java) {
    workingDir = File(rustDir)
    val cargo = if (userHome != null) {
        "$userHome/.cargo/bin/wasm-pack"
    } else {
        "wasm-pack"
    }

    commandLine(cargo, "build", "--target", "web")
}

val rustWasmBuild by tasks.creating(Sync::class.java) {
    group = "Rust"
    dependsOn(rustWasmAssemble)

    from("$rustDir/pkg")
    include("*.ts", "*.js", "*.wasm", "*.json")
    into(wasmRustLib)
}

tasks.withType(KotlinJsCompile::class.java) {
    dependsOn(rustWasmBuild)
}

val rustNativeAssemble by tasks.creating(Exec::class.java) {
    workingDir = File(rustDir)
    val cargo = if (userHome != null) {
        "$userHome/.cargo/bin/cargo"
    } else {
        "cargo"
    }

    commandLine(
        cargo,
        "build",
        "--release",
        "--features",
        "native",
        "--target",
        "aarch64-apple-ios",
        "--target",
        "x86_64-apple-ios",
        "--target",
        "aarch64-apple-ios-sim",
        "--no-default-features"
    )
}

val rustNativeBundle by tasks.creating(Exec::class.java) {
    dependsOn(rustNativeAssemble)
    workingDir = File(rustDir)
    commandLine(
        "lipo",
        "-create",
        // "target/aarch64-apple-ios/release/libbigint_arithmetic.a",
        "target/aarch64-apple-ios-sim/release/libbigint_arithmetic.a",
        "target/x86_64-apple-ios/release/libbigint_arithmetic.a",
        "-output",
        "target/libbigint_arithmetic.a"
    )
}

val rustNativeHeaders by tasks.creating(Exec::class.java) {
    dependsOn(rustNativeBundle)
    workingDir = File(rustDir)
    commandLine(
        "cbindgen",
        "-l",
        "C",
        "-o",
        "target/libbigint_arithmetic.h"
    )
}

tasks.withType(KotlinCompile::class.java) {
    dependsOn("cargoBuild", rustJvmBuild)
}

val projectPackage = "io.bitpogo.krump.bignumber"

android {
    namespace = projectPackage
    ndkVersion = "25.1.8937393"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }

    defaultConfig {
        ndk.abiFilters.addAll(
            listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        )
    }
}
kotlin {
    android()

    jvm()

    js(IR) {
        compilations {
            this.forEach {
                it.compileKotlinTask.kotlinOptions.sourceMap = true
                it.compileKotlinTask.kotlinOptions.metaInfo = true

                if (it.name == "main") {
                    it.compileKotlinTask.kotlinOptions.main = "call"
                }
            }
        }

        browser {
            testTask {
                useKarma {
                    useChromeHeadlessNoSandbox()
                }
            }
        }
    }

    iosx {
        val libraryName = "bigint_arithmetic"
        val libraryPath = "$projectDir/rsrc/target"
        val frameworksPath = "$libraryPath"

        compilations.getByName("main") {
            cinterops.create("Rechenwerk") {
                packageName = "io.bitpogo.krump.rechenwerk"

                val interopTask = tasks[interopProcessingTaskName]
                // TODO
                // interopTask.dependsOn(rustNativeHeaders)

                // Path to .def file
                defFile("$projectDir/src/nativeInterop/cinterop/Rechenwerk.def")
                includeDirs(libraryPath)
                compilerOpts("-DNS_FORMAT_ARGUMENT(A)=")
            }
        }

        compilations.getByName("test") {
            cinterops.create("Rechenwerk") {
                packageName = "io.bitpogo.krump.rechenwerk"

                val interopTask = tasks[interopProcessingTaskName]
                // TODO
                // interopTask.dependsOn(rustNativeHeaders)

                // Path to .def file
                defFile("$projectDir/src/nativeInterop/cinterop/Rechenwerk.def")
                includeDirs.headerFilterOnly(libraryPath)
                compilerOpts("-DNS_FORMAT_ARGUMENT(A)=")
            }
        }

        binaries.all {
            linkerOpts(
                "-rpath", "$frameworksPath",
                // "-w",
                "-L$libraryPath", "-l$libraryName",
                // "-F$frameworksPath", "-framework", "bigint_arithmetic"
            )
        }
    }
    ensureAppleDeviceCompatibility()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.ExperimentalUnsignedTypes")
                optIn("kotlin.RequiresOptIn")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.kotlin.stdlib)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(antibytesCatalog.common.test.kotlin.core)
                implementation(libs.testUtils.core)
                implementation(libs.testUtils.annotations)
                implementation(libs.testUtils.coroutine)
                implementation(libs.kfixture)
                implementation(libs.kmock)
            }
        }

        val concurrentMain by creating {
            dependsOn(commonMain)
        }

        val concurrentTest by creating {
            dependsOn(commonTest)
        }

        val androidMain by getting {
            dependsOn(concurrentMain)

            dependencies {
                implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk8)
            }
        }

        setupAndroidTest()
        val androidAndroidTestRelease by getting {
            dependsOn(concurrentTest)
        }
        val androidTestFixturesDebug by getting {
            dependsOn(concurrentTest)
        }
        val androidTestFixturesRelease by getting {
            dependsOn(concurrentTest)
        }

        val androidTestFixtures by getting {
            dependsOn(concurrentTest)
        }

        val androidTest by getting {
            dependsOn(concurrentTest)

            dependencies {
                implementation(antibytesCatalog.android.test.junit.core)
                implementation(antibytesCatalog.jvm.test.kotlin.junit4)
                implementation(antibytesCatalog.android.test.robolectric)
            }
        }

        val androidAndroidTest by getting {
            dependsOn(concurrentTest)
            dependencies {
                implementation(antibytesCatalog.jvm.test.junit.junit4)
                implementation(antibytesCatalog.android.test.compose.junit4)
                implementation(antibytesCatalog.android.test.espresso.core)
                implementation(antibytesCatalog.android.test.uiAutomator)
            }
        }

        val jvmMain by getting {
            dependsOn(concurrentMain)
            resources.srcDir(jvmRustLib)

            dependencies {
                implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk)
                implementation(libs.nativeBundler)
                implementation(antibytesCatalog.jvm.slf4j.noop)
                implementation(antibytesCatalog.jvm.slf4j.api)
            }
        }
        val jvmTest by getting {
            dependsOn(concurrentTest)
            dependencies {
                implementation(antibytesCatalog.jvm.test.kotlin.core)
                implementation(antibytesCatalog.jvm.test.junit.junit4)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(antibytesCatalog.js.kotlin.stdlib)
                implementation(antibytesCatalog.js.kotlinx.nodeJs)
                implementation(antibytesCatalog.js.kotlinx.coroutines.core)

                nodePackage(antibytesCatalog.node.copyWebpackPlugin)
                implementation(npm("bigint_arithmetic", File(wasmRustLib)))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(antibytesCatalog.js.test.kotlin.core)
                implementation(antibytesCatalog.js.test.kotlinx.coroutines)
            }
        }

        val iosMain by getting {
            dependsOn(concurrentMain)
        }
        val iosTest by getting {
            dependsOn(concurrentTest)
        }
    }
}

kmock {
    rootPackage = projectPackage
    freezeOnDefault = false
}

tasks.named("jvmTest", Test::class.java) {
    systemProperty("java.library.path", jvmRustLib)
}

tasks.named("jvmProcessResources") {
    dependsOn(rustJvmBuild)
}

tasks.named("clean") {
    doLast {
        File("$rustDir/target").deleteRecursively()
        File("$rustDir/pkg").deleteRecursively()
    }
}
