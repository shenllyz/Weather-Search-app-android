
package com.example.weatherapp.view.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R

class DetailActivity : AppCompatActivity() {
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
            val tweetText = "Check Out $cityName’s Weather! It is $temperature°F! #CSCI571WeatherSearch"
            val tweetUrl = "https://twitter.com/intent/tweet?text=${Uri.encode(tweetText)}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl))
            startActivity(intent)
        }
    }
}