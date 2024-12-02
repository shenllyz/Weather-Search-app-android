package com.example.weatherapp.repository

import com.example.weatherapp.model.ApiResponse
import com.example.weatherapp.model.DailyWeather
import com.example.weatherapp.model.HourlyWeather

class WeatherRepository {

    fun parseDailyWeather(apiResponse: ApiResponse): List<DailyWeather> {
        val dailyTimelines = apiResponse.data.timelines.filter { it.timestep == "1d" }

        return if (dailyTimelines.isNotEmpty()) {
            val dailyData = mutableListOf<DailyWeather>()
            dailyTimelines.forEach { dailyTimeline ->
                dailyTimeline.intervals.forEach { interval ->
                    val entry = DailyWeather(
                        date = interval.startTime,
                        values = interval.values
                    )
                    dailyData.add(entry)
                }
            }
            println(dailyData)
            dailyData
        } else {
            println("No daily timeline found in data")
            emptyList()
        }
    }

    fun parseHourlyWeather(apiResponse: ApiResponse): List<HourlyWeather> {
        val hourlyTimelines = apiResponse.data.timelines.filter { it.timestep == "1h" }

        return if (hourlyTimelines.isNotEmpty()) {
            val hourlyData = mutableListOf<HourlyWeather>()
            hourlyTimelines.forEach { hourlyTimeline ->
                hourlyTimeline.intervals.forEach { interval ->
                    val entry = HourlyWeather(
                        startTime = interval.startTime,
                        values = interval.values
                    )
                    hourlyData.add(entry)
                }
            }
            println(hourlyData)
            hourlyData
        } else {
            println("No hourly timeline found in data")
            emptyList()
        }
    }
}
