package com.example.weatherapp.utils

import com.example.weatherapp.R

object WeatherUtils {
    private val weatherDescriptions = mapOf(
        0 to "Unknown",
        1000 to "Clear",
        1100 to "Mostly Clear",
        1101 to "Partly Cloudy",
        1102 to "Mostly Cloudy",
        1001 to "Cloudy",
        2000 to "Fog",
        2100 to "Light Fog",
        4000 to "Drizzle",
        4001 to "Rain",
        4200 to "Light Rain",
        4201 to "Heavy Rain",
        5000 to "Snow",
        5001 to "Flurries",
        5100 to "Light Snow",
        5101 to "Heavy Snow",
        6000 to "Freezing Drizzle",
        6001 to "Freezing Rain",
        6200 to "Light Freezing Rain",
        6201 to "Heavy Freezing Rain",
        7000 to "Ice Pellets",
        7101 to "Heavy Ice Pellets",
        7102 to "Light Ice Pellets",
        8000 to "Thunderstorm"
    )

    private val weatherIcons = mapOf(
        0 to "Unknown",
        1000 to R.drawable.clear_day,
        1100 to R.drawable.mostly_clear_day,
        1101 to R.drawable.partly_cloudy_day,
        1102 to R.drawable.mostly_cloudy,
        1001 to R.drawable.cloudy,
        2000 to R.drawable.fog,
        2100 to R.drawable.fog_light,
        4000 to R.drawable.drizzle,
        4001 to R.drawable.rain,
        4200 to R.drawable.rain_light,
        4201 to R.drawable.rain_heavy,
        5000 to R.drawable.snow,
        5001 to R.drawable.flurries,
        5100 to R.drawable.snow_light,
        5101 to R.drawable.snow_heavy,
        6000 to R.drawable.freezing_drizzle,
        6001 to R.drawable.freezing_rain,
        6200 to R.drawable.freezing_rain_light,
        6201 to R.drawable.freezing_rain_heavy,
        7000 to R.drawable.ice_pellets,
        7101 to R.drawable.ice_pellets_heavy,
        7102 to R.drawable.ice_pellets_light,
        8000 to R.drawable.tstorm
    )

    fun getWeatherDescription(weatherCode: Int): String {
        return weatherDescriptions[weatherCode] ?: "Unknown"
    }

    fun getWeatherIcon(weatherCode: Int): Int {
        return weatherIcons[weatherCode] as Int
    }
}