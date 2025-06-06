plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.roomwordsample"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.roomwordsample"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.room.runtime)
            annotationProcessor (libs.room.compiler)
            testImplementation (libs.room.testing)

// Composants de cycle de vie
            implementation (libs.lifecycle.viewmodel)
            implementation (libs.lifecycle.livedata)
            implementation (libs.lifecycle.common.java8)
}
