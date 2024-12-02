package com.example.weatherapp.model

data class ApiResponse(
    val data: Data
)

data class Data(
    val timelines: List<Timeline>
)

data class Timeline(
    val timestep: String,
    val intervals: List<Interval>
)

data class Interval(
    val startTime: String,
    val values: Map<String, Any>
)
