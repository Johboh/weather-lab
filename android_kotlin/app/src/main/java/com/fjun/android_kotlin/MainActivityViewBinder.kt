package com.fjun.android_kotlin

interface MainActivityViewBinder {
    fun setTemperature(temperature: String)
    fun showSomethingWentWrong(error: String)
    fun showWaitingForPosition()
    fun showWaitingForWeather()
    fun setImageUrl(imageUrl: String)
}