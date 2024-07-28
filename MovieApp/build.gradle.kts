plugins {
    id("com.android.application") version Versions.applicationVersion apply false
    id("org.jetbrains.kotlin.android") version Versions.kotlinVersion apply false
    id("com.google.gms.google-services") version Versions.googleServicesVersion  apply false
    id("com.google.dagger.hilt.android") version Versions.daggerHiltVersion apply false

    // id("com.google.dagger.hilt-android-gradle-plugin") version Versions.daggerHiltVersion apply false
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version Versions.secretsGradleVersion apply false
    id("androidx.navigation.safeargs") version Versions.navigationVersion apply false
}
