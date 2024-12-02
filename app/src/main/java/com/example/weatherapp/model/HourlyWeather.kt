package com.example.weatherapp.model

data class HourlyWeather(
    val startTime: String,
    val values: Map<String, Any>
)
