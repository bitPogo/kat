import org.jetbrains.compose.desktop.application.dsl.TargetFormat

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

val packageName = "tech.antibytes.awesomecats.desktop.app"

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.ui)
                implementation(compose.material)

                implementation(projects.frontend.sharedUi)
                implementation(projects.frontend.sharedFeature)
                implementation(projects.frontend.coroutineWrapper)
                implementation(libs.sdk)
                implementation(antibytesCatalog.common.ktor.client.logging)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.testUtils.core)
                implementation(libs.testUtils.coroutine)
                implementation(libs.kfixture)
                implementation(libs.kmock)
                implementation(antibytesCatalog.jvm.test.junit.core)
            }
        }
    }
}

kmock {
    rootPackage = packageName
}

compose.desktop {
    application {
        mainClass = "$packageName.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "AwesomeCats"
            packageVersion = "1.0.0"

            windows {
                menuGroup = "Compose Examples"
                // see https://wixtoolset.org/documentation/manual/v3/howtos/general/generate_guids.html
                upgradeUuid = "9ba7453f-8486-4e08-9885-89458fc705eb"
            }
        }
    }
}
