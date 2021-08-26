package com.powilliam.weather.domain.repositories

import android.content.Context
import androidx.preference.PreferenceManager
import com.powilliam.weather.domain.models.UnitOfMeasurement
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SettingsRepository @Inject constructor(@ApplicationContext private val applicationContext: Context) {
    private val sharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(applicationContext)

    fun getSelectedUnitOfMeasurement(): UnitOfMeasurement {
        return when (sharedPreferences.getString("unit_of_measurement", null)) {
            "metric" -> UnitOfMeasurement.Metric
            "imperial" -> UnitOfMeasurement.Imperial
            else -> UnitOfMeasurement.Standard
        }
    }
}