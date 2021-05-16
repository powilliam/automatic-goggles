package com.powilliam.weather

import androidx.lifecycle.*
import com.powilliam.weather.domain.http.OpenWeatherService
import com.powilliam.weather.domain.models.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.await
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor (private val weatherService: OpenWeatherService) : ViewModel() {

    private var _state: MutableLiveData<ViewModelState> = MutableLiveData(ViewModelState.IDLE)
    val state: LiveData<ViewModelState>
        get() = _state

    fun getWeatherFromCurrentLocation() = viewModelScope.launch {
        _state.value = ViewModelState.InProgress
        try {
            val weather = weatherService
                .getWeatherFromGeographicCoordinates(lat = "-7.507167", lon = "-63.028309")
                .await()
            _state.value = ViewModelState.Success(weather = weather)
        } catch (exception: Exception) {
            _state.value = ViewModelState.Failed(reason = "Failed when trying to get weather details")
        }
    }
}

sealed class ViewModelState {
    object IDLE : ViewModelState()
    object InProgress : ViewModelState()
    data class Success(val weather: Weather) : ViewModelState()
    data class Failed(val reason: String) : ViewModelState()
}