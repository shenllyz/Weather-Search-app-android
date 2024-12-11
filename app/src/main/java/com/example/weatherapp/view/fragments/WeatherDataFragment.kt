package com.example.weatherapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.R
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.highsoft.highcharts.common.hichartsclasses.*
import com.highsoft.highcharts.core.HIChartView
import com.highsoft.highcharts.common.HIColor
import com.highsoft.highcharts.core.HIFunction

class WeatherDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chartView: HIChartView = view.findViewById(R.id.hc_weatherdata)
        val precipitation = arguments?.getInt("precipitation") ?: 0
        val humidity = arguments?.getInt("humidity") ?: 0
        val cloudCover = arguments?.getInt("cloud_cover") ?: 0

        renderChart(chartView, precipitation, humidity, cloudCover)
    }

    private fun renderChart(chartView: HIChartView, precipitation: Int, humidity: Int, cloudCover: Int) {

        val options = HIOptions()

        val chart = HIChart().apply {
            type = "solidgauge"
            events = HIEvents().apply {
                render = HIFunction(renderIconsString)
            }
        }
        options.chart = chart

        val title = HITitle().apply {
            text = "Stat Summary"
            style = HICSSObject().apply {
                fontSize = "24px"
            }
        }
        options.title = title

        val tooltip = HITooltip().apply {
            useHTML = true
            borderWidth = 0
            backgroundColor = HIColor.initWithName("none")
            shadow = HIShadowOptionsObject().apply { enabled = true }
            style = HICSSObject().apply {
                fontSize = "16px"
                textAlign = "center"
            }
            pointFormat = """
        <div style="text-align:center;">
          <div>{series.name}</div>
          <div style="font-size:2em; color:{point.color}; font-weight:bold;">{point.y}%</div>
        </div>
    """.trimIndent()

            positioner = HIFunction(
                "function (labelWidth) {" +
                        "   return {" +
                        "       x: (this.chart.chartWidth - labelWidth) /2," +
                        "       y: (this.chart.plotHeight / 2) + 15" +
                        "   };" +
                        "}"
            )
        }
        options.tooltip = tooltip

        val pane = HIPane().apply {
            startAngle = 0
            endAngle = 360
            background = arrayListOf(
                HIBackground().apply {
                    outerRadius = "112%"
                    innerRadius = "88%"
                    backgroundColor = HIColor.initWithRGBA(130, 238, 106, 0.35)
                    borderWidth = 0
                },
                HIBackground().apply {
                    outerRadius = "87%"
                    innerRadius = "63%"
                    backgroundColor = HIColor.initWithRGBA(44, 175, 254, 0.35)
                    borderWidth = 0
                },
                HIBackground().apply {
                    outerRadius = "62%"
                    innerRadius = "38%"
                    backgroundColor = HIColor.initWithRGBA(255, 129, 93, 0.35)
                    borderWidth = 0
                }
            )
        }
        options.pane = arrayListOf(pane)

        val yAxis = HIYAxis().apply {
            min = 0
            max = 100
            lineWidth = 0
            tickPositions = arrayListOf()
        }
        options.yAxis = arrayListOf(yAxis)

        val plotOptions = HIPlotOptions().apply {
            solidgauge = HISolidgauge().apply {
                dataLabels = arrayListOf(HIDataLabels().apply {
                    enabled = false
                })
                linecap = "round"
                stickyTracking = false
                rounded = true
            }
        }
        options.plotOptions = plotOptions

        val solidgauge1 = HISolidgauge().apply {
            name = "cloudCover"
            data = arrayListOf(HIData().apply {
                color = HIColor.initWithRGB(130, 238, 106)
                radius = "112%"
                innerRadius = "88%"
                y = cloudCover
            })
        }

        val solidgauge2 = HISolidgauge().apply {
            name = "precipitation"
            data = arrayListOf(HIData().apply {
                color = HIColor.initWithRGB(44, 175, 254)
                radius = "87%"
                innerRadius = "63%"
                y = precipitation
            })
        }

        val solidgauge3 = HISolidgauge().apply {
            name = "humidity"
            data = arrayListOf(HIData().apply {
                color = HIColor.initWithRGB(255, 129, 93)
                radius = "62%"
                innerRadius = "38%"
                y = humidity
            })
        }

        options.series = arrayListOf(solidgauge1, solidgauge2, solidgauge3)

        chartView.options = options

    }

    private val renderIconsString = "function renderIcons() {" +
            "if(!this.series[0].icon) {" +
            "this.series[0].icon = this.renderer.path(['M', -8, 0, 'L', 8, 0, 'M', 0, -8, 'L', 8, 0, 0, 8]).attr({'stroke': '#303030','stroke-linecap': 'round','stroke-linejoin': 'round','stroke-width': 2,'zIndex': 10}).add(this.series[2].group);}" +
            "this.series[0].icon.translate(this.chartWidth / 2 - 10,this.plotHeight / 2 - this.series[0].points[0].shapeArgs.innerR -(this.series[0].points[0].shapeArgs.r - this.series[0].points[0].shapeArgs.innerR) / 2);" +
            "if(!this.series[1].icon) {" +
            "this.series[1].icon = this.renderer.path(['M', -8, 0, 'L', 8, 0, 'M', 0, -8, 'L', 8, 0, 0, 8,'M', 8, -8, 'L', 16, 0, 8, 8]).attr({'stroke': '#ffffff','stroke-linecap': 'round','stroke-linejoin': 'round','stroke-width': 2,'zIndex': 10}).add(this.series[2].group);}" +
            "this.series[1].icon.translate(this.chartWidth / 2 - 10,this.plotHeight / 2 - this.series[1].points[0].shapeArgs.innerR -(this.series[1].points[0].shapeArgs.r - this.series[1].points[0].shapeArgs.innerR) / 2);" +
            "if(!this.series[2].icon) {" +
            "this.series[2].icon = this.renderer.path(['M', 0, 8, 'L', 0, -8, 'M', -8, 0, 'L', 0, -8, 8, 0]).attr({'stroke': '#303030','stroke-linecap': 'round','stroke-linejoin': 'round','stroke-width': 2,'zIndex': 10}).add(this.series[2].group);}" +
            "this.series[2].icon.translate(this.chartWidth / 2 - 10,this.plotHeight / 2 - this.series[2].points[0].shapeArgs.innerR -(this.series[2].points[0].shapeArgs.r - this.series[2].points[0].shapeArgs.innerR) / 2);}"
}