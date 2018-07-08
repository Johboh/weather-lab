package com.fjun.android_kotlin

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent

class MainActivityPresenter(private val gpsLocationListener: GpsLocationListener) : LifecycleObserver {

    private var viewBinder: MainActivityViewBinder? = null

    fun setViewBinder(viewBinder: MainActivityViewBinder) {
        this.viewBinder = viewBinder
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        viewBinder!!.showWaitingForPosition()
        gpsLocationListener.registerListener { location ->
            viewBinder!!.showWaitingForWeather()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        gpsLocationListener.unregisterListener()
    }
}