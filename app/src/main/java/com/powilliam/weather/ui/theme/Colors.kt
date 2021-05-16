package com.powilliam.weather.ui.theme

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

private val Purple200 = Color(0xFFBB86FC)
private val Purple500 = Color(0xFF6200EE)
private val Purple700 = Color(0xFF3700B3)
private val Teal200 = Color(0xFF03DAC5)
private val Teal700 = Color(0xFF018786)
private val Black = Color(0xFF121212)
private val White = Color(0xFFFFFFFF)

val WeatherLightColors = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    onPrimary = White,
    secondary = Teal200,
    secondaryVariant = Teal700,
    onSecondary = Black,
    background = White,
    onBackground = Black
)
val WeatherDarkColors = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    onPrimary = Black,
    secondary = Teal200,
    secondaryVariant = Teal200,
    onSecondary = Black,
    background = Black,
    onBackground = White
)