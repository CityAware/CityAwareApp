


buildscript {
    dependencies {
        classpath(libs.google.services)
        // Tal class
        //d
        val nav_version = "2.7.6"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.gms.google-services") version "4.4.1" apply false

}
