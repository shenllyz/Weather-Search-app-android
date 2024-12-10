// TodayFragment.kt
package com.example.weatherapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.weatherapp.R

class TodayFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_today, container, false)

        val windSpeed = arguments?.getString("wind_speed")
        val pressure = arguments?.getString("pressure")
        val precipitation = arguments?.getInt("precipitation")?:0
        val temperature = arguments?.getString("temperature")
        val weatherDesc = arguments?.getString("weather_desc")
        val humidity = arguments?.getInt("humidity")?:0
        val visibility = arguments?.getString("visibility")
        val cloudCover = arguments?.getInt("cloud_cover")?:0
        val ozone = arguments?.getString("ozone")
        val weatherIcon = arguments?.getInt("weather_icon")

        val weatherIconImageView: ImageView = view.findViewById(R.id.today_weather_icon)
        val windSpeedTextView: TextView = view.findViewById(R.id.today_wind_speed)
        val pressureTextView: TextView = view.findViewById(R.id.today_pressure)
        val precipitationTextView: TextView = view.findViewById(R.id.today_precipitation)
        val temperatureTextView: TextView = view.findViewById(R.id.today_temperature)
        val weatherDescTextView: TextView = view.findViewById(R.id.today_weather_desc)
        val humidityTextView: TextView = view.findViewById(R.id.today_humidity)
        val visibilityTextView: TextView = view.findViewById(R.id.today_visibility)
        val cloudCoverTextView: TextView = view.findViewById(R.id.today_cloud_cover)
        val ozoneTextView: TextView = view.findViewById(R.id.today_ozone)

        windSpeedTextView.text = "${windSpeed}mph"
        pressureTextView.text = "${pressure}inHg"
        precipitationTextView.text = "$precipitation%"
        temperatureTextView.text = "$temperature"
        weatherDescTextView.text = weatherDesc
        humidityTextView.text = "$humidity%"
        visibilityTextView.text = "${visibility}mi"
        cloudCoverTextView.text = "$cloudCover%"
        ozoneTextView.text = "$ozone"
        weatherIconImageView.setImageResource(weatherIcon ?: 0)
        return view
    }
}