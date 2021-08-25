package com.powilliam.weather

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
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
        binding.executePendingBindings()

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<EmptyFragment>(R.id.fragment_container_view)
        }

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
                is ViewModelState.InProgress -> replaceCurrentFragmentOnContainerView<LoadingFragment>()
                is ViewModelState.Success -> replaceCurrentFragmentOnContainerView<WeatherFragment>(
                    bundleOf(WeatherFragment.TEMPERATURE_KEY to state.weather.main.temp)
                )
                else -> replaceCurrentFragmentOnContainerView<EmptyFragment>()
            }
        }
    }

    private inline fun <reified T : Fragment> replaceCurrentFragmentOnContainerView(args: Bundle? = null) {
        supportFragmentManager.commit {
            replace<T>(R.id.fragment_container_view, args = args)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}