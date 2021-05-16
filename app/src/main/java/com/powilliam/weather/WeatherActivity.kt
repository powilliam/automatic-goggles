package com.powilliam.weather

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.powilliam.weather.databinding.ActivityWeatherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil
            .setContentView(this, R.layout.activity_weather)
        binding.lifecycleOwner = this

        binding.floatingActionButton.setOnClickListener {
            weatherViewModel.getWeatherFromCurrentLocation()
        }
    }

    override fun onStart() {
        super.onStart()
        observeWeatherViewModelState()
        weatherViewModel.getWeatherFromCurrentLocation()
    }

    private fun observeWeatherViewModelState() {
        weatherViewModel.state.observe(this) { state: ViewModelState ->
            binding.state = state

            when (state) {
                is ViewModelState.Failed -> {
                    Snackbar
                        .make(this, binding.coordinatorLayout, state.reason, Snackbar.LENGTH_LONG)
                        .show()
                }
                else -> {}
            }
        }
    }
}