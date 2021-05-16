package com.powilliam.weather

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
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
import com.powilliam.weather.domain.models.Weather
import com.powilliam.weather.ui.theme.WeatherTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias OnFailure = suspend CoroutineScope.(reason: String) -> Unit

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
private fun WeatherActivityContent(weatherViewModel: WeatherViewModel) {
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
            Scenes(
                state = state.value ?: ViewModelState.IDLE,
                onFailure = {
                    snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Short)
                }
            )
        }
    }
}

class FakeWeatherProvider : PreviewParameterProvider<Weather> {

    override val values: Sequence<Weather>
        get() = sequenceOf(Weather(main = Weather.Main(temp = 32.0)))
}

@Composable
private fun CenteredContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
        content = content
    )
}

@Preview
@Composable
private fun LoadingScene(modifier: Modifier = Modifier) {
    CenteredContent(
        modifier = modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            strokeWidth = 2.dp,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview
@Composable
private fun SuccessScene(
    @PreviewParameter(
        provider = FakeWeatherProvider::class
    )
    weather: Weather,
    modifier: Modifier = Modifier
) {
    CenteredContent(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column {
            Text(text = "Outside", style = MaterialTheme.typography.body2)
            Text(text = "${weather.main.temp}°C", style = MaterialTheme.typography.h4)
        }
    }
}

@Composable
private fun Scenes(
    state: ViewModelState,
    onFailure: OnFailure,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    Crossfade(targetState = state) { state: ViewModelState ->
        when (state) {
            is ViewModelState.InProgress -> {
                LoadingScene()
            }
            is ViewModelState.Success -> {
                SuccessScene(weather = state.weather)
            }
            is ViewModelState.Failed -> {
                coroutineScope.launch {
                    onFailure(state.reason)
                }
            }
            else -> {}
        }
    }
}