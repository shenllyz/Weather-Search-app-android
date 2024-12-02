package com.example.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.model.ApiResponse
import com.example.weatherapp.model.DailyWeather
import com.example.weatherapp.model.HourlyWeather
import com.example.weatherapp.repository.WeatherRepository

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _dailyWeather = MutableLiveData<List<DailyWeather>>()
    val dailyWeather: LiveData<List<DailyWeather>> get() = _dailyWeather

    private val _hourlyWeather = MutableLiveData<List<HourlyWeather>>()
    val hourlyWeather: LiveData<List<HourlyWeather>> get() = _hourlyWeather

    fun loadDailyWeather(apiResponse: ApiResponse) {
        _dailyWeather.value = repository.parseDailyWeather(apiResponse)
    }

    fun loadHourlyWeather(apiResponse: ApiResponse) {
        _hourlyWeather.value = repository.parseHourlyWeather(apiResponse)
    }
}
