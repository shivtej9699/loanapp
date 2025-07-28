plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android") version "2.48"
    id("kotlin-kapt")
    alias(libs.plugins.google.gms.google.services)
    //id("com.google.gms.google-services")
}

android {
    namespace = "com.example.kssloanapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kssloanapp"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation ("androidx.navigation:navigation-compose:2.7.5")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation ("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("com.squareup:javapoet:1.12.1")
    implementation ("androidx.compose.ui:ui:1.6.1")
    implementation ("androidx.compose.material3:material3:1.2.0")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.6.1")
    implementation ("androidx.compose.foundation:foundation:1.6.1")
    implementation ("androidx.navigation:navigation-compose:2.7.7")

        // Firebase dependencies
//        implementation("com.google.firebase:firebase-bom:32.7.0")
//        implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
    implementation("com.google.firebase:firebase-analytics-ktx")

        // Firebase dependencies
        implementation("com.google.firebase:firebase-bom:32.7.0")
        implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.google.firebase:firebase-storage-ktx:20.3.0")
//        implementation("com.google.firebase:firebase-auth")
//        implementation("com.google.firebase:firebase-firestore")
//        implementation("com.google.firebase:firebase-messaging") // For FCM
//        implementation("com.google.firebase:firebase-database")  // For Realtime Database
//        implementation("com.google.firebase:firebase-config")    // For Remote Config
    }



