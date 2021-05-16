package com.powilliam.weather.bindings

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.powilliam.weather.ViewModelState

@BindingAdapter( "state", "isWeatherText")
fun bindStateAndWeatherToTextView(view: TextView, state: ViewModelState?, isWeatherText: Boolean) {
    when (state) {
        is ViewModelState.Success -> {
            view.visibility = View.VISIBLE

            if (isWeatherText) {
                val formattedText = "${state.weather.main.temp}°C"
                view.text = formattedText
            }
        }
        else -> {
            view.visibility = View.GONE
        }
    }
}