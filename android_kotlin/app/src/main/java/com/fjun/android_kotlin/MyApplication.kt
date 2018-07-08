package com.fjun.android_kotlin

import android.app.Application
import org.koin.android.ext.android.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin(this, listOf(myModule))
    }
}