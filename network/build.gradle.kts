plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    alias(libs.plugins.jetbrains.kotlin.android.serialization)

    // alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.google.dagger.hilt)
}

android {
    namespace = "com.app.currency.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.google.dagger.hilt)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.io.ktor.client)
    implementation(libs.io.ktor.client.serialization)
    implementation(libs.io.ktor.client.content.negotiation)
    implementation(libs.io.ktor.serialization.kotlinx.json)

}