/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
import tech.antibytes.gradle.dependency.settings.localGithub

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        val antibytesPlugins = "^tech\\.antibytes\\.[\\.a-z\\-]+"
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-snapshots/main/snapshots")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-rolling-releases/main/rolling")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
    }
}

plugins {
    id("tech.antibytes.gradle.dependency.settings") version "283c93a"
}

includeBuild("setup")

include(
    ":rust:bigint",
    ":shared",
    ":backend",
    ":frontend:shared-feature",
    ":frontend:shared-ui",
    ":frontend:coroutine-wrapper",
    ":frontend:android",
    ":frontend:js",
    ":frontend:desktop",
    ":frontend:ios:compose"
)

buildCache {
    localGithub()
}

rootProject.name = "Awesome_Random_Cats"
