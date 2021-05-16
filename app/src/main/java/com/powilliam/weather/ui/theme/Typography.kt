package com.powilliam.weather.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.powilliam.weather.R

private val Karla = FontFamily(
    Font(R.font.karla_regular, FontWeight.Normal),
    Font(R.font.karla_medium, FontWeight.Medium)
)

val WeatherTypography = Typography(
    defaultFontFamily = Karla
)