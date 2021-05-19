package com.powilliam.weather.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun WeatherTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDark) WeatherDarkColors else WeatherLightColors

    MaterialTheme(
        colors = colorScheme,
        typography = WeatherTypography,
        content = content
    )
}