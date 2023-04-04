/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

import tech.antibytes.gradle.configuration.apple.ensureAppleDeviceCompatibility
import tech.antibytes.gradle.configuration.sourcesets.setupAndroidTest
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import tech.antibytes.gradle.configuration.sourcesets.iosx
import tech.antibytes.gradle.configuration.sourcesets.nativeCoroutine

plugins {
    alias(antibytesCatalog.plugins.gradle.antibytes.kmpConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.androidLibraryConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)

    alias(libs.plugins.kmock)
}

val projectPackage = "tech.antibytes.awesomecats.viewmodel"

android {
    namespace = projectPackage

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
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

    iosx()
    ensureAppleDeviceCompatibility()

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlinx.coroutines.DelicateCoroutinesApi")
                optIn("tech.antibytes.kmock.KMock")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.kotlin.stdlib)
                implementation(antibytesCatalog.common.kotlinx.coroutines.core)
                implementation(antibytesCatalog.common.koin.core)
                implementation(antibytesCatalog.common.ktor.client.core)
                implementation(antibytesCatalog.common.ktor.client.logging)
                implementation(antibytesCatalog.common.ktor.client.contentNegotiation)
                implementation(antibytesCatalog.common.ktor.client.json)
                implementation(antibytesCatalog.common.ktor.serialization.json)

                implementation(projects.rust.bigint)

                implementation(antibytesCatalog.common.kotlinx.serialization.core)
                implementation(antibytesCatalog.common.kotlinx.serialization.json)

                api(projects.shared)
                api(projects.frontend.coroutineWrapper)
            }
        }
        val commonTest by getting {
            kotlin.srcDir("${buildDir.absolutePath.trimEnd('/')}/generated/antibytes/commonTest/kotlin")

            dependencies {
                implementation(antibytesCatalog.common.test.kotlin.core)
                implementation(antibytesCatalog.common.test.ktor.client.mockClient)

                implementation(libs.kmock)
                implementation(libs.kfixture)
                implementation(libs.testUtils.core)
                implementation(libs.testUtils.annotations)
                implementation(libs.testUtils.coroutine)
                implementation(libs.testUtils.ktor)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk8)
                implementation(antibytesCatalog.android.ktor.client)
                implementation(antibytesCatalog.jvm.ktor.client.okhttp)

                implementation(antibytesCatalog.android.ktx.viewmodel.core)
                implementation(antibytesCatalog.android.ktx.viewmodel.compose)
            }
        }

        setupAndroidTest()
        val androidTest by getting {
            dependencies {
                implementation(antibytesCatalog.android.test.junit.core)
                implementation(antibytesCatalog.jvm.test.kotlin.junit4)
                implementation(antibytesCatalog.android.test.ktx)
                implementation(antibytesCatalog.android.test.robolectric)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk)
                implementation(antibytesCatalog.jvm.ktor.client.core)
                implementation(antibytesCatalog.jvm.ktor.client.okhttp)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(antibytesCatalog.jvm.test.kotlin.core)
                implementation(antibytesCatalog.jvm.test.junit.junit4)
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(antibytesCatalog.js.kotlin.stdlib)
                implementation(antibytesCatalog.js.kotlinx.nodeJs)
                implementation(antibytesCatalog.js.ktor.client.core)
                implementation(
                    npm(
                        "bigint_arithmetic",
                        File("${rootProject.projectDir.absolutePath.trimEnd('/')}/rust/bigint/wasm")
                    )
                )
                implementation(projects.rust.bigint)
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(antibytesCatalog.js.test.kotlin.core)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(antibytesCatalog.common.ktor.client.cio)
            }
        }
    }
}

kmock {
    rootPackage = projectPackage
    allowInterfaces = true
    useBuildInProxiesOn = setOf(
        "io.bitpogo.krump.bignumber.BigUIntegerContract.BigUInteger"
    )
    preventResolvingOfAliases = setOf(
        "tech.antibytes.awesomecats.store.data.PurrMultiplier"
    )
}

tasks.withType(Test::class.java) {
    testLogging {
        events(FAILED)
    }
}