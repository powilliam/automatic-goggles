package com.powilliam.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.powilliam.weather.databinding.FragmentWeatherBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentWeatherBinding
    private val temperature by lazy {
        val arguments = requireArguments()
        val formatter = DecimalFormat("##.#")
        val temperature = arguments.getDouble(TEMPERATURE_KEY)
        return@lazy formatter.format(temperature)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater)
        binding.temperatureText.text = getString(
            R.string.celsius_temperature,
            temperature
        )
        return binding.root
    }

    companion object {
        const val TEMPERATURE_KEY = "temperature"
    }
}