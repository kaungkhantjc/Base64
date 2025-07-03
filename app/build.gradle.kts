plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.jcoder.base64"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jcoder.base64"
        minSdk = 14
        targetSdk = 36
        versionCode = 3
        versionName = "1.0.2"

        buildFeatures {
            viewBinding = true
            buildConfig = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
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
}

dependencies {
    implementation(libs.guava)
}