package com.powilliam.weather.bindings

import androidx.databinding.BindingAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.powilliam.weather.ViewModelState

@BindingAdapter("state")
fun bindStateToProgressIndicator(view: CircularProgressIndicator, state: ViewModelState?) {
    when (state) {
        is ViewModelState.InProgress -> {
            view.show()
        }
        else -> {
            view.hide()
        }
    }
}