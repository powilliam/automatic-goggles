package com.powilliam.weather.domain.models

data class Weather(val main: Main) {
    data class Main (
        val temp: Double
    )
}