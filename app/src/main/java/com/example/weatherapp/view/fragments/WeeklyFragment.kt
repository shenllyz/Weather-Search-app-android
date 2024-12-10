package com.example.weatherapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
import com.highsoft.highcharts.core.*
import com.highsoft.highcharts.common.hichartsclasses.*
import com.highsoft.highcharts.common.HIColor

class WeeklyFragment : Fragment() {


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

        var title = HITitle().apply {
            text = "Temperature variation by day"
        }
        options.title = title

        val xAxis = HIXAxis().apply {
            type = "datetime"
        }
        options.xAxis = arrayListOf(xAxis)

        val yAxis = HIYAxis()
        val yAxisTitle = HITitle()
        yAxisTitle.text = "Temperature(°F)"
        yAxis.setTitle(yAxisTitle)
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
            lineColor = HIColor.initWithHexValue("@color/temperature_line_color")
            color = fillColor
        }

        options.series = arrayListOf(series)
        chartView.options = options
    }

}