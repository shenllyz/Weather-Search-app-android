package com.example.weatherapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.utils.WeatherUtils
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class ForecastAdapter(private val dailyWeatherList: List<JSONObject>) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val ivWeatherIcon: ImageView = itemView.findViewById(R.id.iv_weather_icon)
        val tvMinTemp: TextView = itemView.findViewById(R.id.tv_min_temp)
        val tvMaxTemp: TextView = itemView.findViewById(R.id.tv_max_temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dailyWeather = dailyWeatherList[position]
        val startTime = dailyWeather.getString("startTime")
        val values = dailyWeather.getJSONObject("values")
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(startTime)
        val formattedDate = outputFormat.format(date)


        val temperatureMin = values.getDouble("temperatureMin").roundToInt()
        val temperatureMax = values.getDouble("temperatureMax").roundToInt()

        val weatherCode = values.getInt("weatherCode")
        val weatherIconResId = WeatherUtils.getWeatherIcon(weatherCode)

        holder.tvDate.text = formattedDate
        holder.tvMinTemp.text = "$temperatureMin"
        holder.tvMaxTemp.text = "$temperatureMax"
        holder.ivWeatherIcon.setImageResource(weatherIconResId)
    }

    override fun getItemCount(): Int {
        return dailyWeatherList.size
    }
    fun getTemperatureChartOptions(): List<Triple<Long, Int, Int>> {
        val temperatureData = mutableListOf<Triple<Long, Int, Int>>()
        dailyWeatherList.forEach { dailyWeather ->
            val startTime = dailyWeather.getString("startTime")
            val values = dailyWeather.getJSONObject("values")

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val date = inputFormat.parse(startTime)
            val formattedDate = outputFormat.format(date)


            val formattedDateInMillis = outputFormat.parse(formattedDate)?.time ?: 0L

            val temperatureMin = values.getDouble("temperatureMin").roundToInt()
            val temperatureMax = values.getDouble("temperatureMax").roundToInt()

            temperatureData.add(Triple(formattedDateInMillis, temperatureMin, temperatureMax))
        }
        return temperatureData
    }

}