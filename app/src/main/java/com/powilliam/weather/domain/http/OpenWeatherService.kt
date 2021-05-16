package com.powilliam.weather.domain.http

import com.powilliam.weather.BuildConfig
import com.powilliam.weather.domain.models.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("data/2.5/weather")
    fun getWeatherFromGeographicCoordinates(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String? = "metric",
        @Query("appid") appid: String? = BuildConfig.OPEN_WEATHER_API_KEY
    ): Call<Weather>

    companion object {
        const val BASE_URL = "https://api.openweathermap.org"
    }
}