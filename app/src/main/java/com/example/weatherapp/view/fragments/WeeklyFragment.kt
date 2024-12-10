package com.example.weatherapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.highsoft.highcharts.core.*
import com.highsoft.highcharts.common.hichartsclasses.*
import com.example.weatherapp.viewmodel.WeatherViewModel
import androidx.fragment.app.activityViewModels
import com.highsoft.highcharts.common.HIColor

class WeeklyFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weekly, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chartView: HIChartView = view.findViewById(R.id.hc_temperature_range)

        val temperatureChartOptions = arguments?.getSerializable("temperature_chart_options") as? List<Triple<Long, Int, Int>>
        temperatureChartOptions?.let {
            renderChart(chartView, it)
        }
    }

    private fun renderChart(chartView: HIChartView, data: List<Triple<Long, Int, Int>>) {
        val options = HIOptions()

        val chart = HIChart().apply {
            type = "arearange"
        }
        options.chart = chart

        val title = HITitle().apply {
            text = "Temperature variation by day"
        }
        options.title = title

        val xAxis = HIXAxis().apply {
            type = "datetime"
        }
        options.xAxis = arrayListOf(xAxis)

        val yAxis = HIYAxis()
        options.yAxis = arrayListOf(yAxis)

        val tooltip = HITooltip().apply {
            val shadowOptions = HIShadowOptionsObject().apply { enabled = true }
            shadow = shadowOptions
            valueSuffix = "°F"
        }
        options.tooltip = tooltip

        val legend = HILegend().apply {
            enabled = false
        }
        options.legend = legend




        val series = HIArearange().apply {
            name = "Temperatures"
            val seriesData = data.map { arrayOf<Any>(it.first, it.second, it.third) }
            this.data = ArrayList(seriesData)

            // 设置线条颜色（可选）
            lineColor = HIColor.initWithHexValue("#27a3fc")

            // 设置渐变填充色
            color = fillColor


        }

        options.series = arrayListOf(series)
        chartView.options = options
    }

}