@file:Suppress("UNCHECKED_CAST")

plugins {
    kotlin("kapt")
    id("com.android.application")
    id("kotlin-android")
}

repositories {
    mavenCentral()
    google()
}

android {
    compileSdk = 31
    buildToolsVersion = "31"

    defaultConfig {
        applicationId = "com.shiroumi.ShiroPlayer"
        minSdk = 24
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = mutableListOf(
            "-Xopt-in=" +
                    "androidx.compose.ui.ExperimentalComposeUiApi," +
                    "kotlin.contracts.ExperimentalContracts," +
                    "androidx.compose.animation.ExperimentalAnimationApi"
        )
    }

    buildFeatures {
        aidl = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["composeVer"] as String
    }

    packagingOptions {
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}

typealias impl = DependencyHandlerScope.() -> Unit

dependencies {
    rootProject.extra["implementation"].cast<impl>()(this)
}

fun <T> Any?.cast(): T {
    return this as T
}
