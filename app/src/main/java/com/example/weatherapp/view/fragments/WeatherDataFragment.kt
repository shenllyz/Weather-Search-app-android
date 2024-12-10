package com.example.weatherapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherapp.R
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

        val options = HIOptions()

        val chart = HIChart().apply {
            type = "solidgauge"
            events = HIEvents().apply {
                render = HIFunction(renderIconsString)
            }
        }
        options.chart = chart

        val title = HITitle().apply {
            text = "Activity"
            style = HICSSObject().apply {
                fontSize = "24px"
            }
        }
        options.title = title

        val tooltip = HITooltip().apply {
            borderWidth = 0
            backgroundColor = HIColor.initWithName("none")
            shadow = HIShadowOptionsObject().apply { enabled = false }
            style = HICSSObject().apply {
                fontSize = "16px"
            }
            pointFormat = "{series.name}<br><span style=\"font-size:2em; color: {point.color}; font-weight: bold\">{point.y}%</span>"
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
                    backgroundColor = HIColor.initWithRGBA(106, 165, 231, 0.35)
                    borderWidth = 0
                },
                HIBackground().apply {
                    outerRadius = "87%"
                    innerRadius = "63%"
                    backgroundColor = HIColor.initWithRGBA(51, 52, 56, 0.35)
                    borderWidth = 0
                },
                HIBackground().apply {
                    outerRadius = "62%"
                    innerRadius = "38%"
                    backgroundColor = HIColor.initWithRGBA(130, 238, 106, 0.35)
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
            name = "Move"
            data = arrayListOf(HIData().apply {
                color = HIColor.initWithRGB(106, 165, 231)
                radius = "112%"
                innerRadius = "88%"
                y = 80
            })
        }

        val solidgauge2 = HISolidgauge().apply {
            name = "Exercise"
            data = arrayListOf(HIData().apply {
                color = HIColor.initWithRGB(51, 52, 56)
                radius = "87%"
                innerRadius = "63%"
                y = 65
            })
        }

        val solidgauge3 = HISolidgauge().apply {
            name = "Stand"
            data = arrayListOf(HIData().apply {
                color = HIColor.initWithRGB(130, 238, 106)
                radius = "62%"
                innerRadius = "38%"
                y = 50
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