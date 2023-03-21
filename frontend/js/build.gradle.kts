/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig.*
import tech.antibytes.gradle.dependency.helper.nodePackage

plugins {
    id(antibytesCatalog.plugins.kotlin.js.get().pluginId)
    alias(libs.plugins.kmock)
}

kotlin {
    js(IR) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                mode = if (project.hasProperty("prod")) {
                    Mode.PRODUCTION
                } else {
                    Mode.DEVELOPMENT
                }
            }
        }
        useCommonJs()
    }
}

val packageName = "tech.antibytes.awesomecats.js.app"

kmock {
    rootPackage = packageName
}

dependencies {
    implementation(antibytesCatalog.js.kotlin.stdlib)
    implementation(antibytesCatalog.js.kotlinx.coroutines.core)
    implementation(enforcedPlatform(antibytesCatalog.js.kotlin.wrappers.bom))
    implementation(antibytesCatalog.js.kotlin.wrappers.react.main.get().module.toString())
    implementation(antibytesCatalog.js.kotlin.wrappers.react.dom.get().module.toString())
    implementation(antibytesCatalog.js.kotlin.wrappers.extensions.get().module.toString())
    implementation(projects.frontend.sharedFeature)

    testImplementation(antibytesCatalog.js.test.kotlin.core)
    testImplementation(libs.testUtils.core)
    testImplementation(libs.testUtils.annotations)
    testImplementation(libs.testUtils.coroutine)
    testImplementation(libs.kfixture)
    testImplementation(libs.kmock)
}
