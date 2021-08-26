package com.powilliam.weather

import android.content.Context
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.powilliam.weather.domain.http.OpenWeatherService
import com.powilliam.weather.domain.models.UnitOfMeasurement
import com.powilliam.weather.domain.models.Weather
import com.powilliam.weather.domain.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.await
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val weatherService: OpenWeatherService
) : ViewModel() {
    private var _state: MutableStateFlow<ViewModelState> = MutableStateFlow(ViewModelState.IDLE)
    val state: StateFlow<ViewModelState>
        get() = _state

    fun getWeatherFromCurrentLocation() = viewModelScope.launch {
        _state.emit(ViewModelState.InProgress)
        try {
            val unitOfMeasurement = settingsRepository.getSelectedUnitOfMeasurement()
            val weather = weatherService
                .getWeatherFromGeographicCoordinates(
                    lat = "-7.507167",
                    lon = "-63.028309",
                    units = unitOfMeasurement.system
                )
                .await()
            _state.emit(ViewModelState.Success(weather = weather, unit = unitOfMeasurement))
        } catch (exception: Exception) {
            _state.emit(ViewModelState.Failed(reason = "Failed when trying to get weather details"))
        }
    }
}

sealed class ViewModelState {
    object IDLE : ViewModelState()
    object InProgress : ViewModelState()
    data class Success(val weather: Weather, val unit: UnitOfMeasurement) : ViewModelState()
    data class Failed(val reason: String) : ViewModelState()
}