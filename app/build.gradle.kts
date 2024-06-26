plugins {

    id("org.jetbrains.kotlin.android")
    id("com.android.application")
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.cityaware"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cityaware"
        minSdk = 26
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



    implementation ("com.google.firebase:firebase-firestore")
    implementation ("com.google.firebase:firebase-storage")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation("androidx.navigation:navigation-fragment:2.5.3")
    implementation("androidx.navigation:navigation-ui:2.5.3")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation ("com.google.firebase:firebase-database:20.1.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation ("com.google.firebase:firebase-auth:21.0.2")
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.squareup.retrofit2:retrofit:2.1.0")
    implementation ("com.google.code.gson:gson:2.6.2")
    implementation ("com.squareup.retrofit2:converter-gson:2.1.0")
    implementation ("androidx.appcompat:appcompat:1.5.1")
    implementation ("com.google.android.material:material:1.7.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-maps:17.0.0")
    implementation("com.google.android.gms:play-services-location:17.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.1.0")
    implementation("com.google.code.gson:gson:2.6.2")
    implementation("com.squareup.retrofit2:converter-gson:2.1.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-runtime:2.5.3")
    implementation("androidx.annotation:annotation:1.5.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("com.google.firebase:firebase-auth:21.0.2")
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("com.google.firebase:firebase-database:20.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.4")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")
    var nav_version = "2.5.3"
    implementation ("androidx.navigation:navigation-fragment:$nav_version")
    implementation ("androidx.navigation:navigation-ui:$nav_version")

    var room_version = "2.4.3"
    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor ("androidx.room:room-compiler:$room_version")
    implementation (platform("com.google.firebase:firebase-bom:31.1.1"))
    implementation ("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-firestore")
    implementation ("com.google.firebase:firebase-storage")
    implementation ("com.squareup.picasso:picasso:2.8")

}