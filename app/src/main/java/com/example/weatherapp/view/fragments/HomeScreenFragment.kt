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
import androidx.fragment.app.activityViewModels
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
    private val weatherViewModel: WeatherViewModel by activityViewModels()
    private lateinit var forecastAdapter: ForecastAdapter
    private var temperatureChartOptions = mutableListOf<Triple<Long, Int, Int>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("HomeScreenFragment", "onCreateView called")
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

        forecastRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        forecastAdapter = ForecastAdapter(emptyList())
        forecastRecyclerView.adapter = forecastAdapter

        weatherViewModel.formattedAddress.observe(viewLifecycleOwner) { address ->
            cityNameTextView.text = address
        }

        // 当currentWeather有更新时，执行UI更新操作
        weatherViewModel.currentWeather.observe(viewLifecycleOwner) { currentWeather ->
            Log.d("HomeScreenFragment", "currentWeather observed")
            updateWeatherAttributes(
                currentWeather,
                currentTemperatureTextView,
                weatherIconImageView,
                humidityTextView,
                windSpeedTextView,
                visibilityTextView,
                pressureTextView,
                weatherSummaryTextView
            )

            // 数据加载完成，设置isLoading=false，从而让MainActivity显示出ViewPager并隐藏loadingpage
            weatherViewModel.setLoading(false)
        }

        weatherViewModel.dailyWeather.observe(viewLifecycleOwner) { dailyWeather ->
            forecastAdapter = ForecastAdapter(dailyWeather)
            temperatureChartOptions = forecastAdapter.getTemperatureChartOptions().toMutableList()
            Log.d("HomeScreenFragment", "Temperature Chart Options: $temperatureChartOptions")
            forecastRecyclerView.adapter = forecastAdapter
        }

        // 如果是初次加载，需要获取初始位置信息
        if (weatherViewModel.currentWeather.value == null) {
            weatherViewModel.loadIpInfo()
        }

        currentWeatherCard.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            val cityName = cityNameTextView.text.toString()
            val temperature = currentTemperatureTextView.text.toString()
            val weatherDesc = weatherSummaryTextView.text.toString()
            val currentWeatherData = weatherViewModel.currentWeather.value
            val values = currentWeatherData?.getJSONObject("values")

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
        // 不在这里设置Loading=true，因为我们希望在初始加载时就已经在Loading状态
        Log.d("HomeScreenFragment", "Loading render start: ${weatherViewModel.isLoading.value}")

        val values = currentWeather.getJSONObject("values")
        val temperature = values.getDouble("temperature").roundToInt()
        val humidity = values.getInt("humidity")
        val windSpeed = values.getDouble("windSpeed")
        val visibility = values.getDouble("visibility")
        val pressure = values.getDouble("pressureSeaLevel")
        val weatherCode = values.getInt("weatherCode")

        currentTemperatureTextView.text = "${temperature}°F"
        humidityTextView.text = "$humidity%"
        windSpeedTextView.text = "${windSpeed}mph"
        visibilityTextView.text = "${visibility}mi"
        pressureTextView.text = "${pressure}inHg"
        weatherSummaryTextView.text = WeatherUtils.getWeatherDescription(weatherCode)

        val weatherIconResId = WeatherUtils.getWeatherIcon(weatherCode)
        weatherIconImageView.setImageResource(weatherIconResId)

        Log.d("HomeScreenFragment", "Loading render end: ${weatherViewModel.isLoading.value}")
        // 在此处不需要再次setLoading，因为数据刚刚加载完成，我们接下来在观察者中setLoading(false)
    }
}
