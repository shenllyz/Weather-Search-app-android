// HomeScreenFragment.kt
package com.example.weatherapp.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.utils.WeatherUtils
import com.example.weatherapp.view.activities.DetailActivity
import com.example.weatherapp.view.adapters.ForecastAdapter
import com.example.weatherapp.viewmodel.WeatherViewModel
import org.json.JSONObject
import kotlin.math.roundToInt

class HomeScreenFragment : Fragment() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_screen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTemperatureTextView: TextView = view.findViewById(R.id.current_temperature)
        val cityNameTextView: TextView = view.findViewById(R.id.city_name)
        val weatherIconImageView: ImageView = view.findViewById(R.id.weather_icon)
        val humidityTextView: TextView = view.findViewById(R.id.humidity)
        val windSpeedTextView: TextView = view.findViewById(R.id.wind_speed)
        val visibilityTextView: TextView = view.findViewById(R.id.visibility)
        val pressureTextView: TextView = view.findViewById(R.id.pressure)
        val weatherSummaryTextView: TextView = view.findViewById(R.id.weather_summary)
        val forecastRecyclerView: RecyclerView = view.findViewById(R.id.forecast_recyclerview)
        val currentWeatherCard: LinearLayout = view.findViewById(R.id.current_weather_card)
        var temperatureChartOptions = mutableListOf<Triple<Long, Int, Int>>()

        forecastRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        forecastAdapter = ForecastAdapter(emptyList())
        forecastRecyclerView.adapter = forecastAdapter

        weatherViewModel.formattedAddress.observe(viewLifecycleOwner, Observer { address ->
            cityNameTextView.text = address
        })

        weatherViewModel.currentWeather.observe(viewLifecycleOwner, Observer { currentWeather ->
            updateWeatherAttributes(currentWeather, currentTemperatureTextView, weatherIconImageView, humidityTextView,
                windSpeedTextView, visibilityTextView, pressureTextView, weatherSummaryTextView)
            Log.d("HomeScreenFragment", "Current Weather: $currentWeather")
        })

        weatherViewModel.dailyWeather.observe(viewLifecycleOwner, Observer { dailyWeather ->
            forecastAdapter = ForecastAdapter(dailyWeather)
            temperatureChartOptions = forecastAdapter.getTemperatureChartOptions().toMutableList()
            Log.d("HomeScreenFragment", "Temperature Chart Options: $temperatureChartOptions")
            forecastRecyclerView.adapter = forecastAdapter
        })

        weatherViewModel.loadIpInfo()

        currentWeatherCard.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            val cityName = cityNameTextView.text.toString()
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
    }
}