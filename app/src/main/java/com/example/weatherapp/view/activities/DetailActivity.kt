package com.example.weatherapp.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.R
import com.example.weatherapp.view.fragments.TodayFragment
import com.example.weatherapp.view.fragments.WeatherDataFragment
import com.example.weatherapp.view.fragments.WeeklyFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        val toolbarTitle: TextView = findViewById(R.id.toolbar_title)
        val cityName = intent.getStringExtra("city_name")


        toolbarTitle.text = cityName

        val xButton: ImageView = findViewById(R.id.xButton)
        xButton.setOnClickListener {
            val temperature = intent.getStringExtra("temperature")
            val tweetText = "Check Out $cityName’s Weather! It is $temperature! #CSCI571WeatherSearch"
            val tweetUrl = "https://twitter.com/intent/tweet?text=${Uri.encode(tweetText)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl))
            startActivity(intent)
        }

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "TODAY"
                1 -> "WEEKLY"
                2 -> "WEATHER DATA"
                else -> null
            }
            tab.icon = when (position) {
                0 -> getDrawable(R.drawable.today)
                1 -> getDrawable(R.drawable.weekly_tab)
                2 -> getDrawable(R.drawable.weather_data_tab)
                else -> null
            }
        }.attach()
    }

    private inner class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TodayFragment().apply {
                    arguments = Bundle().apply {
                        putString("wind_speed", intent.getStringExtra("wind_speed"))
                        putString("temperature", intent.getStringExtra("temperature"))
                        putString("weather_desc", intent.getStringExtra("weather_desc"))
                        putString("pressure", intent.getStringExtra("pressure"))
                        putInt("precipitation", intent.getIntExtra("precipitation", 0))
                        putInt("humidity", intent.getIntExtra("humidity", 0))
                        putString("visibility", intent.getStringExtra("visibility"))
                        putInt("cloud_cover", intent.getIntExtra("cloud_cover", 0))
                        putString("ozone", intent.getStringExtra("ozone"))
                        putInt("weather_icon", intent.getIntExtra("weather_icon", 0))
                    }
                }
                1 -> WeeklyFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable("temperature_chart_options", intent.getSerializableExtra("temperature_chart_options"))
                    }
                }
                2 -> WeatherDataFragment().apply {
                    arguments = Bundle().apply {
                        putInt("precipitation", intent.getIntExtra("precipitation", 0))
                        putInt("humidity", intent.getIntExtra("humidity", 0))
                        putInt("cloud_cover", intent.getIntExtra("cloud_cover", 0))
                    }
                }
                else -> Fragment()
            }
        }
    }
}