/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.jetbrains.compose)
}

val packageName = "tech.antibytes.awesomecats.shared.app"

kotlin {
    android()
    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.ui)
                // Needed only for preview.
                api(compose.preview)
                api(projects.frontend.sharedFeature)
            }
        }
        val androidMain by getting {
            dependencies {
                api(antibytesCatalog.android.ktx.core)
                api(antibytesCatalog.android.appCompact.core)
            }
        }
    }
}

android {
    namespace = packageName
}
