package com.powilliam.weather.bindings

import androidx.databinding.BindingAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.powilliam.weather.ViewModelState

// TODO: Create binding for CircularProgressIndicator
@BindingAdapter("state")
fun bindStateToProgressIndicator(view: CircularProgressIndicator, state: ViewModelState?) {}