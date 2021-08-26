package com.powilliam.weather

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.powilliam.weather.databinding.ActivityWeatherBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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

        binding.toolbar.setOnMenuItemClickListener { onMenuItemClickListener(it) }
        binding.floatingActionButton.setOnClickListener {
            weatherViewModel.getWeatherFromCurrentLocation()
        }
    }

    override fun onStart() {
        super.onStart()
        collectWeatherViewModelState()
        weatherViewModel.getWeatherFromCurrentLocation()
    }

    private fun onMenuItemClickListener(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.settings -> {
                Intent(this, SettingsActivity::class.java).also { settingsActivityIntent ->
                    startActivity(settingsActivityIntent)
                }
                true
            }
            else -> true
        }
    }

    private fun collectWeatherViewModelState() {
        lifecycleScope.launchWhenStarted {
            weatherViewModel.state.collect { state: ViewModelState ->
                binding.state = state
                when (state) {
                    is ViewModelState.Failed -> {
                        Snackbar
                            .make(
                                this@WeatherActivity,
                                binding.coordinatorLayout,
                                state.reason,
                                Snackbar.LENGTH_LONG
                            )
                            .show()
                    }
                    is ViewModelState.InProgress -> replaceCurrentFragmentOnContainerView<LoadingFragment>()
                    is ViewModelState.Success -> replaceCurrentFragmentOnContainerView<WeatherFragment>(
                        bundleOf(
                            WeatherFragment.TEMPERATURE_KEY to state.weather.main.temp,
                            WeatherFragment.UNIT_OF_MEASUREMENT_KEY to state.unit
                        )
                    )
                    else -> replaceCurrentFragmentOnContainerView<EmptyFragment>()
                }
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