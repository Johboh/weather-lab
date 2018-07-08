package com.fjun.android_kotlin

import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

// Koin module
val myModule: Module = applicationContext {
    factory { MainActivityPresenter(get()) }
    bean { MainActivity() as MainActivityViewBinder }
}