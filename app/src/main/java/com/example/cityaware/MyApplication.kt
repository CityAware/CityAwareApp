package com.example.cityaware
import com.google.firebase.FirebaseApp
import android.app.Application
import android.content.Context


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        myContext = applicationContext
    }

    companion object {
       lateinit var myContext: Context
            private set
    }
}

