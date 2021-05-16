package com.powilliam.weather.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object LocationModule {

    @Provides
    fun providesFusedLocationProviderClient(
        @ActivityContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices
            .getFusedLocationProviderClient(context)
    }
}