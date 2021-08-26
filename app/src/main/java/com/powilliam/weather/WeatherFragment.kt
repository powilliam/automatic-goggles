package com.powilliam.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.powilliam.weather.databinding.FragmentWeatherBinding
import com.powilliam.weather.domain.models.UnitOfMeasurement
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private val temperature by lazy {
        val arguments = requireArguments()
        val formatter = DecimalFormat("##.#")
        val temperature = formatter.format(arguments.getDouble(TEMPERATURE_KEY))
        return@lazy when (arguments.getSerializable(UNIT_OF_MEASUREMENT_KEY)) {
            is UnitOfMeasurement.Metric -> getString(R.string.celsius_temperature, temperature)
            is UnitOfMeasurement.Imperial -> getString(R.string.fahrenheit_temperature, temperature)
            else -> getString(R.string.kelvin_temperature, temperature)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater)
        binding.temperatureText.text = temperature
        return binding.root
    }

    companion object {
        const val TEMPERATURE_KEY = "TEMPERATURE_KEY"
        const val UNIT_OF_MEASUREMENT_KEY = "UNIT_OF_MEASUREMENT_KEY"
    }
}