import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import tech.antibytes.gradle.configuration.sourcesets.iosx

/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.jetbrains.compose)
    alias(libs.plugins.kmock)
}

val packageName = "tech.antibytes.awesomecats.shared.app"

kotlin {
    android()
    jvm()
    iosx()
    ensureAppleDeviceCompatibility()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.ui)

                api(projects.frontend.sharedFeature)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(antibytesCatalog.common.test.kotlin.core)

                implementation(libs.testUtils.core)
                implementation(libs.testUtils.annotations)
                implementation(libs.kfixture)
                implementation(libs.kmock)
            }
        }

        val androidMain by getting {
            dependencies {
                // Needed only for preview.
                api(compose.preview)
                api(antibytesCatalog.android.ktx.core)
                api(antibytesCatalog.android.appCompact.core)
            }
        }

        val jvmMain by getting {
            dependencies {
                // Needed only for preview.
                api(compose.preview)
            }
        }
    }
}

kmock {
    rootPackage = packageName
}

android {
    namespace = packageName
}


