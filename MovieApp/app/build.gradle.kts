plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.movieapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.movieapp"
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
    buildFeatures{
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Pagging
    implementation("androidx.paging:paging-runtime-ktx:${Versions.paging_version}")

    // Room
    implementation("androidx.room:room-runtime:${Versions.room_version}")
    implementation("androidx.room:room-ktx:${Versions.room_version}")
    ksp("androidx.room:room-compiler:${Versions.room_version}")

    // Simple Search View
    implementation("com.github.Ferfalk:SimpleSearchView:${Versions.simpleSearchViewVersion}")

    // Gson
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}")

    // OKHttp
    implementation(platform("com.squareup.okhttp3:okhttp-bom:${Versions.okhttpVersion}"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // Glide
    // implementation("com.github.bumptech.glide:ksp:${Versions.glideVersion}")
    implementation("com.github.bumptech.glide:glide:${Versions.glideVersion}")
    ksp("com.github.bumptech.glide:compiler:${Versions.glideVersion}")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${Versions.navigationVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${Versions.navigationVersion}")

    // Lifecycle (usar o emit no viewModel)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleVersion}")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleVersion}")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:${Versions.splashVersion}")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:${Versions.firebaseVersion}"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-storage")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:${Versions.daggerHiltVersion}")
    ksp("com.google.dagger:hilt-compiler:${Versions.daggerHiltVersion}")

    // Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
