package com.example.cityaware

import android.app.Application
import android.content.Context

class MyApplication constructor() : Application() {
    public override fun onCreate() {
        super.onCreate()
        myContext = getApplicationContext()
    }

    companion object {
        var myContext: Context? = null
            private set
    }
}