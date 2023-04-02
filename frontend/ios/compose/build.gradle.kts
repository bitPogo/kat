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
    kotlin("native.cocoapods")
}

val packageName = "tech.antibytes.awesomecats.ios.app"

kotlin {
    iosx()
    ensureAppleDeviceCompatibility()

    cocoapods {
        summary = "iOS Cat Screens"
        homepage = "https://www.example.com"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../swift/AwesomeCats/Podfile")
        framework {
            baseName = "compose"
            isStatic = true
        }
    }

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

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
