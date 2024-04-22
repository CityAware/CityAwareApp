package com.example.cityaware

import android.app.Application
import android.content.Context


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        getMyContext = applicationContext
    }

    companion object {
        var getMyContext: Context? = null
            private set
    }
}
