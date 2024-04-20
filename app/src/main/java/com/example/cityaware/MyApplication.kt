package com.example.cityaware

import android.app.Application
import android.content.Context


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        myContext = applicationContext
    }

    companion object {
        var myContext: Context? = null
            private set
    }
}