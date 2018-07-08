package com.fjun.android_kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), MainActivityViewBinder {

    // Inject MyPresenter
    val presenter: MainActivityPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup presenter: Attach lifecycle
        lifecycle.addObserver(presenter)
    }

    override fun showSomethingWentWrong(error: String) {
        status.text = getString(R.string.something_went_wrong, error)
    }

    override fun showWaitingForPosition() {
        status.text = getString(R.string.waiting_for_position)
    }

    override fun showWaitingForWeather() {
        status.text = getString(R.string.waiting_for_weather)
    }

    override fun setTemperature(temperature: String) {
        status.text = getString(R.string.temperature, temperature)
    }

    override fun setImageUrl(imageUrl: String) {
        Glide.with(this).load(imageUrl).into(icon)
    }
}
