package com.powilliam.weather.di

import com.powilliam.weather.domain.http.OpenWeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpModule {

    @Singleton
    @Provides
    fun provideOpenWeatherService(): OpenWeatherService {
        return Retrofit
            .Builder()
            .baseUrl(OpenWeatherService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherService::class.java)
    }
}