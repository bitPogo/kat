import tech.antibytes.gradle.configuration.runtime.AntiBytesMainConfigurationTask
import org.jetbrains.kotlin.gradle.dsl.KotlinCompile
import java.util.Properties

plugins {
    id("io.ktor.plugin") version "2.1.3"
    id(antibytesCatalog.plugins.kotlin.jvm.get().pluginId)
    // alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)
    alias(antibytesCatalog.plugins.kotlinx.serialization)
    alias(libs.plugins.kmock)
}

val moduleSpace = "tech.antibytes.awesomecats.backend"

/*
kotlin {
    val hostOs = System.getProperty("os.name")
    val arch = System.getProperty("os.arch")
    val nativeTarget = when {
        hostOs == "Mac OS X" && arch == "x86_64" -> macosX64()
        hostOs == "Mac OS X" && arch == "aarch64" -> macosArm64()
        hostOs == "Linux" -> linuxX64()
        // Other supported targets are listed here: https://ktor.io/docs/native-server.html#targets
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.coroutines.DelicateCoroutinesApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.kotlin.stdlib)
                implementation(antibytesCatalog.common.ktor.server.core)
                implementation(antibytesCatalog.common.ktor.server.cio)
                implementation(antibytesCatalog.common.ktor.server.contentNegotiation)

                implementation(antibytesCatalog.common.ktor.serialization.json)

                implementation(antibytesCatalog.common.kotlinx.serialization.core)
                implementation(antibytesCatalog.common.kotlinx.serialization.json)

                implementation(antibytesCatalog.common.koin.core)
                implementation(libs.sdk)
                implementation(libs.kotlinResult)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(antibytesCatalog.common.test.kotlin.core)
                implementation(antibytesCatalog.common.test.kotlinx.coroutines)
                implementation(libs.testUtils.core)
                implementation(libs.testUtils.annotations)
                implementation(libs.kfixture)
                implementation(libs.kmock)
            }
        }
    }
}*/

dependencies {
    implementation(antibytesCatalog.common.kotlin.stdlib)
    implementation(antibytesCatalog.common.ktor.server.core)
    implementation(antibytesCatalog.common.ktor.client.logging)
    implementation(antibytesCatalog.common.ktor.server.cio)
    implementation(antibytesCatalog.common.ktor.server.contentNegotiation)

    implementation(antibytesCatalog.common.ktor.serialization.json)

    implementation(antibytesCatalog.common.kotlinx.serialization.core)
    implementation(antibytesCatalog.common.kotlinx.serialization.json)

    implementation(antibytesCatalog.common.koin.core)
    implementation(libs.sdk)
    implementation(libs.kotlinResult)
    implementation(projects.shared)

    testImplementation(antibytesCatalog.common.test.kotlin.core)
    testImplementation(antibytesCatalog.common.test.kotlinx.coroutines)
    testImplementation(libs.testUtils.core)
    // testImplementation(libs.testUtils.annotations)
    testImplementation(libs.kfixture)
    testImplementation(libs.kmock)

    testImplementation(antibytesCatalog.jvm.test.kotlin.core)
    testImplementation(antibytesCatalog.jvm.test.junit.core)
}

kmock {
    rootPackage = moduleSpace
    allowInterfaces = true
}

val apiKey: String = if(project.ext.has("localProperties")) {
    (project.ext["localProperties"] as? Properties)?.get("gpr.pixabay.apikey").toString()
} else {
    project.properties["gpr.pixabay.apikey"].toString()
}

val provideConfig: Task by tasks.creating(AntiBytesMainConfigurationTask::class) {
    packageName.set("$moduleSpace.config")
    this.stringFields.set(
        mapOf(
            "apiKey" to apiKey,
        )
    )
    this.integerFields.set(
        mapOf(
            "seed" to 42,
        )
    )
}

tasks.withType(KotlinCompile::class.java) {
    this.dependsOn(provideConfig)
}

group = moduleSpace
version = "0.0.1"
application {
    mainClass.set("$moduleSpace.ServerKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks.test {
    useJUnitPlatform()
}

configure<SourceSetContainer> {
    main {
        java.srcDirs(
            "src/main/kotlin",
            "build/generated/antibytes/main/kotlin",
        )
    }
}
