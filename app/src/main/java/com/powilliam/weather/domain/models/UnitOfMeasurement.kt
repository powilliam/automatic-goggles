package com.powilliam.weather.domain.models

import java.io.Serializable

sealed class UnitOfMeasurement(val system: String? = null) : Serializable {
    object Imperial : UnitOfMeasurement("imperial")
    object Metric : UnitOfMeasurement("metric")
    object Standard : UnitOfMeasurement()
}