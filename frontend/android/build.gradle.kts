/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

plugins {
    id(antibytesCatalog.plugins.kotlin.android.get().pluginId)

    alias(antibytesCatalog.plugins.gradle.antibytes.androidApplicationConfiguration)
    alias(antibytesCatalog.plugins.gradle.antibytes.coverage)

    alias(libs.plugins.kmock)
}

val packageName = "tech.antibytes.awesomecats.android.app"

kmock {
    rootPackage = packageName
}

android {
    namespace = packageName

    defaultConfig {
        applicationId = packageName
        versionCode = 1
        versionName = "1.0"
        minSdk = 24

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = antibytesCatalog.versions.android.compose.compiler.get()
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            isMinifyEnabled = false
            matchingFallbacks.add("release")
        }
    }
}

dependencies {
    implementation(antibytesCatalog.jvm.kotlin.stdlib.jdk8)

    implementation(antibytesCatalog.android.ktx.core)
    implementation(antibytesCatalog.android.ktx.viewmodel.core)
    implementation(antibytesCatalog.android.ktx.viewmodel.compose)

    implementation(antibytesCatalog.android.appCompact.core)

    implementation(antibytesCatalog.android.ktx.activity.compose)
    implementation(antibytesCatalog.android.compose.ui.core)
    implementation(antibytesCatalog.android.material.compose.core)
    implementation(antibytesCatalog.android.compose.foundation.core)

    implementation(antibytesCatalog.android.material.core)
    implementation(antibytesCatalog.android.material.compose.icons)
    implementation(antibytesCatalog.android.material.compose.extendedIcons)

    implementation(projects.frontend.sharedUi)
    implementation(projects.frontend.sharedFeature)
    implementation(projects.frontend.coroutineWrapper)
    implementation(libs.sdk)
    implementation(antibytesCatalog.common.ktor.client.logging)

    implementation(antibytesCatalog.android.coil.core)
    implementation(antibytesCatalog.android.coil.compose)

    testImplementation(libs.testUtils.core)
    testImplementation(libs.testUtils.coroutine)
    testImplementation(libs.kfixture)
    testImplementation(libs.kmock)
    testImplementation(antibytesCatalog.android.test.junit.core)
    testImplementation(antibytesCatalog.jvm.test.junit.junit4)

    // Debug
    debugImplementation(antibytesCatalog.android.compose.ui.tooling.core)
    debugImplementation(antibytesCatalog.android.test.compose.manifest)

    androidTestImplementation(antibytesCatalog.android.test.junit.core)
    androidTestImplementation(antibytesCatalog.jvm.test.junit.junit4)
    androidTestImplementation(antibytesCatalog.android.test.compose.junit4)
    androidTestImplementation(antibytesCatalog.android.test.espresso.core)
    androidTestImplementation(antibytesCatalog.android.test.uiAutomator)

    androidTestImplementation(libs.testUtils.core)
    androidTestImplementation(libs.kfixture)
    androidTestImplementation(libs.kmock)
}
