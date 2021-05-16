package com.powilliam.weather.bindings

import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.powilliam.weather.ViewModelState

@BindingAdapter("state")
fun bindStateToFloatingActionButton(view: FloatingActionButton, state: ViewModelState?) {
    when (state) {
        is ViewModelState.InProgress -> {
            view.hide()
        }
        else -> {
            view.show()
        }
    }
}