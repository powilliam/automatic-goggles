package com.powilliam.weather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.powilliam.weather.databinding.ActivityWeatherBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding
    @Inject lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val weatherViewModel: WeatherViewModel by viewModels()

    // TODO: Set View.OnClickListener to FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil
            .setContentView(this, R.layout.activity_weather)
        binding.lifecycleOwner = this

        observeWeatherViewModelState()
    }

    override fun onStart() {
        super.onStart()
        getWeatherFromCurrentLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when {
            (requestCode == LOCATION_PERMISSION_CODE
                    && grantResults.isNotEmpty()
                    && PackageManager.PERMISSION_GRANTED in grantResults) -> getWeatherFromCurrentLocation()
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    // TODO: Implement
    private fun observeWeatherViewModelState() {}

    private fun getWeatherFromCurrentLocation() {
        when {
            (ContextCompat.checkSelfPermission(this, PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED) -> {
                Log.i("OnStart", "Granted")
                val task = fusedLocationProviderClient.getCurrentLocation(
                    LocationRequest.PRIORITY_HIGH_ACCURACY,
                    object : CancellationToken() {
                        override fun isCancellationRequested(): Boolean {
                            return false
                        }
                        override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                            return this
                        }
                    }
                )

                task.addOnSuccessListener { location: Location? ->
                    location?.let {
                        weatherViewModel.getWeatherFromCurrentLocation(it)
                    }
                }
            }
            else -> {
                Log.i("OnStart", "Unauthorized")
                requestPermissions(PERMISSIONS, LOCATION_PERMISSION_CODE)
            }
        }
    }

    companion object {
        const val LOCATION_PERMISSION_CODE = 101
        val PERMISSIONS: Array<String>
            get() = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    }
}