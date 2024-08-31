plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.uniplanner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.uniplanner"
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Firebase Authentication
    implementation ("com.google.firebase:firebase-auth:21.0.3")

    // Google Sign-In
    implementation ("com.google.android.gms:play-services-auth:20.1.0")

    // RecyclerView for displaying tasks
    implementation ("androidx.recyclerview:recyclerview:1.3.1")


    implementation ("com.google.firebase:firebase-bom:32.1.1")

        // Firebase Authentication
        implementation ("com.google.firebase:firebase-auth")

        // Firebase Realtime Database
        implementation ("com.google.firebase:firebase-database")




    // Date Picker Dialog
    implementation ("com.wdullaer:materialdatetimepicker:4.2.3")

    // Glide for image loading (if needed)
    implementation ("com.github.bumptech.glide:glide:4.15.1")

}
