plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")

    // Safe Args
    id("androidx.navigation.safeargs.kotlin")

    // Dagger Hilt
    id("com.google.dagger.hilt.android")
    // Config. plugin ksp
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.togglebutton"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.togglebutton"
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
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    ksp("com.google.dagger:hilt-android-compiler:2.48.1")

    // Room
    implementation("androidx.room:room-compiler:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
}