package com.example.weatherapp.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.model.FavoriteLocation
import com.example.weatherapp.repository.WeatherRepository
import org.json.JSONObject

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WeatherRepository(application)

    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> get() = _isLoading


    private val _dailyWeather = MutableLiveData<List<JSONObject>>()
    val dailyWeather: LiveData<List<JSONObject>> get() = _dailyWeather

    private val _hourlyWeather = MutableLiveData<List<JSONObject>>()
    val hourlyWeather: LiveData<List<JSONObject>> get() = _hourlyWeather

    private val _currentWeather = MutableLiveData<JSONObject>()
    val currentWeather: LiveData<JSONObject> get() = _currentWeather

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

    private val _favorites = MutableLiveData<List<FavoriteLocation>>()
    val favorites: LiveData<List<FavoriteLocation>> get() = _favorites

    private val _cityAutocomplete = MutableLiveData<List<Pair<String, String>>>()
    val cityAutocomplete: LiveData<List<Pair<String, String>>> get() = _cityAutocomplete

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

    fun loadWeatherData(lat: Double, lon: Double) {
        repository.fetchWeatherData(
            lat = lat,
            lon = lon,
            onSuccess = { response ->
                _weatherData.value = response
                parseDailyWeather(response)
                parseHourlyWeather(response)
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
                _formattedAddress.value = "$city, $state"
                loadWeatherData(lat.toDouble(), lon.toDouble())
            },
            onError = { errorMessage ->
                _error.value = errorMessage
            }
        )
    }

    fun loadFavorites() {
        repository.fetchFavorites(
            context = getApplication(),
            onSuccess = { favorites ->
                _favorites.value = favorites
            },
            onError = { errorMessage ->
                _error.value = errorMessage
            }
        )
    }

    fun loadCityAutocomplete(query: String) {
        repository.fetchCityAutocomplete(
            context = getApplication(),
            query = query,
            onSuccess = { results ->
                _cityAutocomplete.value = results
            },
            onError = { errorMessage ->
                _error.value = errorMessage
            }
        )
    }

    fun addFavorite(location: FavoriteLocation) {
        repository.addFavorite(
            city = location.city,
            state = location.state,
            lat = location.lat,
            lng = location.lng,
            onSuccess = {
                Log.d("WeatherViewModel", "${location.city} was added to favorites")
                Toast.makeText(getApplication(), "${location.city} was added to favorites", Toast.LENGTH_SHORT).show()
            },
            onError = { errorMessage ->
                _error.value = errorMessage
            }
        )
    }
    fun deleteFavorite(location: FavoriteLocation) {
        location.id?.let { id ->
            repository.deleteFavorite(
                id = id,
                onSuccess = {
                    Toast.makeText(getApplication(), "${location.city} was removed from favorites", Toast.LENGTH_SHORT).show()
                },
                onError = { errorMessage ->
                    _error.value = errorMessage
                }
            )
        } ?: run {
            _error.value = "Favorite location ID is missing"
        }
    }
    fun setLoading(isLoading: Boolean) {
        _isLoading.postValue(isLoading)
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
        if (hourlyData.isNotEmpty()) {
            _currentWeather.value = hourlyData[0]
        }
    }
}