package com.example.weatherapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.model.ApiResponse

import org.json.JSONObject
import com.example.weatherapp.repository.WeatherRepository


class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepository(application)


    private val _dailyWeather = MutableLiveData<List<JSONObject>>()
    val dailyWeather: LiveData<List<JSONObject>> get() = _dailyWeather

    private val _hourlyWeather = MutableLiveData<List<JSONObject>>()
    val hourlyWeather: LiveData<List<JSONObject>> get() = _hourlyWeather


    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> get() = _latitude

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double> get() = _longitude

    private val _formattedAddress = MutableLiveData<String>()
    val formattedAddress: LiveData<String> get() = _formattedAddress

    private val _weatherData = MutableLiveData<JSONObject>()
    val weatherData: LiveData<JSONObject> get() = _weatherData

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun loadGeocodingData(address: String) {
        repository.fetchGeocodingData(
            address = address,
            onSuccess = { lat, lon, formattedAddress ->

                _latitude.value = lat
                _longitude.value = lon
                _formattedAddress.value = formattedAddress


                loadWeatherData(lat, lon)
            },
            onError = { errorMessage ->

                _error.value = errorMessage
            }
        )
    }

    // Function to load weather data using latitude and longitude
    fun loadWeatherData(lat: Double, lon: Double) {
        repository.fetchWeatherData(
            lat = lat,
            lon = lon,
            onSuccess = { response ->

                _weatherData.value = response
            },
            onError = { errorMessage ->

                _error.value = errorMessage
            }
        )
    }

    fun loadIpInfo() {
        repository.fetchIpInfo(
            onSuccess = { lat, lon, city, state ->

                _latitude.value = lat.toDouble()
                _longitude.value = lon.toDouble()

                // Fetch weather data using the obtained latitude and longitude
                loadWeatherData(lat.toDouble(), lon.toDouble())
            },
            onError = { errorMessage ->

                _error.value = errorMessage
            }
        )
    }

    private fun parseDailyWeather(response: JSONObject) {
        val dailyData = mutableListOf<JSONObject>()
        val timelines = response.getJSONObject("data").getJSONArray("timelines")

        for (i in 0 until timelines.length()) {
            val timeline = timelines.getJSONObject(i)
            if (timeline.getString("timestep") == "1d") {
                val intervals = timeline.getJSONArray("intervals")
                for (j in 0 until intervals.length()) {
                    dailyData.add(intervals.getJSONObject(j))
                }
            }
        }
        _dailyWeather.value = dailyData
    }
    private fun parseHourlyWeather(response: JSONObject) {
        val hourlyData = mutableListOf<JSONObject>()
        val timelines = response.getJSONObject("data").getJSONArray("timelines")

        for (i in 0 until timelines.length()) {
            val timeline = timelines.getJSONObject(i)
            if (timeline.getString("timestep") == "1h") {
                val intervals = timeline.getJSONArray("intervals")
                for (j in 0 until intervals.length()) {
                    hourlyData.add(intervals.getJSONObject(j))
                }
            }
        }
        _hourlyWeather.value = hourlyData
    }
}
