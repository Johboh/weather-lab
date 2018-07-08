package com.fjun.android_kotlin

import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

// Koin module
val myModule: Module = applicationContext {
    factory { params -> MainActivityPresenter(get { params.values }) }
    factory { params -> GpsLocationListener(params["activity"]) }
}