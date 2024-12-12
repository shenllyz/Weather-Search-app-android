package com.example.weatherapp.model

data class FavoriteLocation(
    val city: String,
    val state: String,
    val lat: Double,
    val lng: Double,
    val id: String? = null
)