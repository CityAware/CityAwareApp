

buildscript {
    repositories {
        google()
        mavenCentral()  // Maven Central repository
    }
    dependencies {
        var nav_version = "2.5.3"
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath ("com.google.gms:google-services:4.3.13")
    }
}
plugins {
    id ("com.android.application") version "7.3.1" apply false
    id ("com.android.library") version "7.3.1" apply false
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    id ("org.jetbrains.kotlin.android") version "1.9.0" apply false
}