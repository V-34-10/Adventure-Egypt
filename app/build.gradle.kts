plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.asiaegypt.adventu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.asiaegypt.adventu"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    implementation("com.appodeal.ads:sdk:3.3.2.0") {
        exclude(group = "com.appodeal.ads.sdk.networks", module = "yandex")
        exclude(group = "com.appodeal.ads.sdk.services", module = "adjust")
        exclude(group = "com.appodeal.ads.sdk.services", module = "appsflyer")
        exclude(group = "com.appodeal.ads.sdk.services", module = "facebook_analytics")
        exclude(group = "com.appodeal.ads.sdk.services", module = "firebase")
        exclude(group = "com.applovin.mediation", module = "yandex-adapter")
    }
    implementation("com.bigossp:max-mediation:4.8.2.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    // Okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.12.0")
    // JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.google.code.gson:gson:2.10.1")
}