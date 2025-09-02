plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")

}

android {
    namespace = "com.example.handy_shopping"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.handy_shopping"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.converter.gson.v290)
    implementation(libs.retrofit.v290)
    implementation(libs.androidx.navigation.fragment.ktx.v235)
    implementation(libs.androidx.navigation.ui.ktx.v235)
    implementation(libs.material.v130)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.transition)
    implementation(libs.google.material.v140)
    implementation(libs.androidx.drawerlayout)
    implementation(libs.glide)
    implementation(libs.gson)
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    implementation(libs.androidx.core.ktx.v160)
    implementation(libs.androidx.appcompat.v131)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.databinding.runtime)
    implementation(libs.androidx.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}