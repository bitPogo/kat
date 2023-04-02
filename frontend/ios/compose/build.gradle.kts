import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import tech.antibytes.gradle.configuration.sourcesets.iosx
import org.jetbrains.compose.experimental.dsl.IOSDevices
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)
    alias(antibytesCatalog.plugins.jetbrains.compose)

    alias(libs.plugins.kmock)
}

val packageName = "tech.antibytes.awesomecats.ios.app"

kotlin {
    iosx("uikit") {
        binaries {
            executable {
                entryPoint = "main"
                freeCompilerArgs += listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal",
                    "-linker-option", "-framework", "-linker-option", "CoreText",
                    "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                )
            }
        }
    }
    ensureAppleDeviceCompatibility()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.ui)
                implementation(compose.material)
                implementation(libs.seikoImage)

                implementation(projects.frontend.sharedUi)
                implementation(projects.frontend.sharedFeature)
                implementation(projects.frontend.coroutineWrapper)
                implementation(libs.sdk)
                implementation(antibytesCatalog.common.ktor.client.logging)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.testUtils.core)
                implementation(libs.testUtils.coroutine)
                implementation(libs.kfixture)
                implementation(libs.kmock)
                implementation(antibytesCatalog.common.test.kotlin.core)
            }
        }
    }
}

kmock {
    rootPackage = packageName
}

compose.experimental {
    web.application {}
    uikit.application {
        bundleIdPrefix = "packageName"
        projectName = "AwesomeCats"
        deployConfigurations {
            simulator("IPhone13") {
                device = IOSDevices.IPHONE_13
            }
            simulator("IPad") {
                device = IOSDevices.IPAD_MINI_6th_Gen
            }
            connectedDevice("Device") {
                teamId="**"
            }
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

kotlin {
    targets.withType<KotlinNativeTarget> {
        binaries.all {
            freeCompilerArgs += "-Xdisable-phases=VerifyBitcode"
        }
    }
}

// TODO: remove when https://youtrack.jetbrains.com/issue/KT-50778 fixed
project.tasks.withType(KotlinJsCompile::class.java).configureEach {
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xir-dce-runtime-diagnostic=log"
    )
}
