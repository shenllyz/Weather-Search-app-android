package com.example.weatherapp.view.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.FavoriteLocation
import com.example.weatherapp.utils.WeatherUtils
import com.example.weatherapp.view.adapters.ForecastAdapter
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject
import kotlin.math.roundToInt

class SearchableActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var forecastAdapter: ForecastAdapter
    private lateinit var loadingPage: View
    private lateinit var searchResultContent: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("SearchableActivity", "SearchableActivity onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val formattedAddress = intent.getStringExtra("formatted_address")
        val latitude = intent.getDoubleExtra("latitude", 0.0)
        val longitude = intent.getDoubleExtra("longitude", 0.0)
        val cityName = intent.getStringExtra("city_name") // Retrieve the city name
        val cityState = cityName?.split(",")?.map { it.trim() }
        val city = cityState?.getOrNull(0) ?: ""
        val state = cityState?.getOrNull(1) ?: ""
        val deleteFab: FloatingActionButton = findViewById(R.id.delete_fab)
        val addFab: FloatingActionButton = findViewById(R.id.add_fab)

        val currentTemperatureTextView: TextView = findViewById(R.id.current_temperature)
        val cityNameTextView: TextView = findViewById(R.id.city_name)
        val weatherIconImageView: ImageView = findViewById(R.id.weather_icon)
        val humidityTextView: TextView = findViewById(R.id.humidity)
        val windSpeedTextView: TextView = findViewById(R.id.wind_speed)
        val visibilityTextView: TextView = findViewById(R.id.visibility)
        val pressureTextView: TextView = findViewById(R.id.pressure)
        val weatherSummaryTextView: TextView = findViewById(R.id.weather_summary)
        val forecastRecyclerView: RecyclerView = findViewById(R.id.forecast_recyclerview)
        val currentWeatherCard: LinearLayout = findViewById(R.id.current_weather_card)
        var temperatureChartOptions = mutableListOf<Triple<Long, Int, Int>>()
        val resultToolbarTitle: TextView = findViewById(R.id.result_toolbar_title)
        val backtoHomeIcon: ImageView = findViewById(R.id.search_result_backButton)
        loadingPage = findViewById(R.id.loading_page)
        searchResultContent = findViewById(R.id.search_result_content)

        forecastRecyclerView.layoutManager = LinearLayoutManager(this)
        forecastAdapter = ForecastAdapter(emptyList())
        forecastRecyclerView.adapter = forecastAdapter

        weatherViewModel.loadWeatherData(latitude, longitude)

        weatherViewModel.currentWeather.observe(this) { currentWeather ->
            updateWeatherAttributes(currentWeather, currentTemperatureTextView, weatherIconImageView, humidityTextView,
                windSpeedTextView, visibilityTextView, pressureTextView, weatherSummaryTextView)
        }

        weatherViewModel.dailyWeather.observe(this) { dailyWeather ->
            forecastAdapter = ForecastAdapter(dailyWeather)
            temperatureChartOptions = forecastAdapter.getTemperatureChartOptions().toMutableList()
            forecastRecyclerView.adapter = forecastAdapter
        }

        weatherViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                loadingPage.visibility = View.VISIBLE
                searchResultContent.visibility = View.GONE
            } else {
                loadingPage.visibility = View.GONE
                searchResultContent.visibility = View.VISIBLE
            }
        }

        resultToolbarTitle.text = formattedAddress
        cityNameTextView.text = cityName // Set the city name
        backtoHomeIcon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        addFab.setOnClickListener {
            val favoriteLocation = FavoriteLocation(city, state, latitude, longitude)
            weatherViewModel.addFavorite(favoriteLocation)
            Log.d("SearchableActivity", "Favorite location added: $favoriteLocation")
        }

        deleteFab.setOnClickListener {
            val favoriteLocation = FavoriteLocation(city, state, latitude, longitude)
            weatherViewModel.deleteFavorite(favoriteLocation)
        }

        currentWeatherCard.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)
            val temperature = currentTemperatureTextView.text.toString()
            val weatherDesc = weatherSummaryTextView.text.toString()
            val currentWeather = weatherViewModel.currentWeather.value
            val values = currentWeather?.getJSONObject("values")
            intent.putExtra("city_name", cityName)
            intent.putExtra("temperature", temperature)
            intent.putExtra("humidity", values?.getInt("humidity"))
            intent.putExtra("wind_speed", values?.getDouble("windSpeed").toString())
            intent.putExtra("visibility", values?.getDouble("visibility").toString())
            intent.putExtra("pressure", values?.getDouble("pressureSeaLevel").toString())
            intent.putExtra("precipitation", values?.getInt("precipitationProbability"))
            intent.putExtra("cloud_cover", values?.getInt("cloudCover"))
            intent.putExtra("ozone", values?.getInt("uvIndex").toString())
            intent.putExtra("weather_desc", weatherDesc)
            intent.putExtra("weather_icon", WeatherUtils.getWeatherIcon(values?.getInt("weatherCode") ?: 0))
            intent.putExtra("temperature_chart_options", ArrayList(temperatureChartOptions))
            startActivity(intent)
        }
    }

    private fun updateWeatherAttributes(
        currentWeather: JSONObject,
        currentTemperatureTextView: TextView,
        weatherIconImageView: ImageView,
        humidityTextView: TextView,
        windSpeedTextView: TextView,
        visibilityTextView: TextView,
        pressureTextView: TextView,
        weatherSummaryTextView: TextView
    ) {
        weatherViewModel.setLoading(true)
        val values = currentWeather.getJSONObject("values")
        val temperature = values.getDouble("temperature").roundToInt()
        val humidity = values.getInt("humidity")
        val windSpeed = values.getDouble("windSpeed")
        val visibility = values.getDouble("visibility")
        val pressure = values.getDouble("pressureSeaLevel")
        val weatherCode = values.getInt("weatherCode")

        // Update the views with the current weather data
        currentTemperatureTextView.text = "${temperature}°F"
        humidityTextView.text = "$humidity%"
        windSpeedTextView.text = "${windSpeed}mph"
        visibilityTextView.text = "${visibility}mi"
        pressureTextView.text = "${pressure}inHg"
        weatherSummaryTextView.text = WeatherUtils.getWeatherDescription(weatherCode)

        val weatherIconResId = WeatherUtils.getWeatherIcon(weatherCode)
        weatherIconImageView.setImageResource(weatherIconResId)
        weatherViewModel.setLoading(false)
    }
}