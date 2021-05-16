package com.powilliam.weather.bindings

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.powilliam.weather.ViewModelState

// TODO: Create binding for TextView
@BindingAdapter( "state", "isWeatherText")
fun bindStateAndWeatherToTextView(view: TextView, state: ViewModelState?, isWeatherText: Boolean) {}