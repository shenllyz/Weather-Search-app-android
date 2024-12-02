package com.example.weatherapp.model

data class DailyWeather(
    val date: String,
    val values: Map<String, Any>
)
