package com.example.cityaware

import android.app.Application
import android.content.Context


class MyApplication : Application() {
    private var context: Context? = null
    fun getMyContext(): Context? {
        return context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
