package com.powilliam.weather

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.powilliam.weather.domain.models.Weather
import com.powilliam.weather.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WeatherActivityContent(weatherViewModel = weatherViewModel)
        }
    }

    override fun onStart() {
        super.onStart()
        weatherViewModel.getWeatherFromCurrentLocation()
    }
}

@Composable
fun WeatherActivityContent(weatherViewModel: WeatherViewModel) {
    val state: State<ViewModelState?> = weatherViewModel.state.observeAsState()
    val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }

    WeatherTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { weatherViewModel.getWeatherFromCurrentLocation() }) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = stringResource(id = R.string.get_weather_details)
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState,
                    snackbar = {
                        Snackbar(
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            Text(text = it.message, style = MaterialTheme.typography.body2)
                        }
                    }
                )
            },
        ) {
            ScreenContent(
                state = state.value ?: ViewModelState.IDLE,
                onFailed = {
                    snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Short)
                }
            )
        }
    }
}

@Composable
fun ScreenContent(
    state: ViewModelState,
    onFailed: suspend CoroutineScope.(reason: String) -> Unit = {},
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        when (state) {
            is ViewModelState.InProgress -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            is ViewModelState.Success -> {
                Column {
                    Text(text = "Outside", style = MaterialTheme.typography.body2)
                    Text(text = "${state.weather.main.temp}°C", style = MaterialTheme.typography.h4)
                }
            }
            is ViewModelState.Failed -> {
                coroutineScope.launch {
                    onFailed(state.reason)
                }
            }
            else -> {}
        }
    }
}

@Preview
@Composable
private fun PreviewWithInProgressState() {
    ScreenContent(state = ViewModelState.InProgress)
}

@Preview
@Composable
private fun PreviewWithSuccessState() {
    val weather = Weather(
        main = Weather.Main(temp = 32.1)
    )
    ScreenContent(state = ViewModelState.Success(weather = weather))
}

@Preview
@Composable
private fun PreviewWithFailedState() {
    ScreenContent(state = ViewModelState.Failed(reason = "Failed"))
}