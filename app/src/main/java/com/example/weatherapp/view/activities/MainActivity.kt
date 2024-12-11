// MainActivity.kt
package com.example.weatherapp.view.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.weatherapp.R
import com.example.weatherapp.view.fragments.HomeScreenFragment
import com.example.weatherapp.view.fragments.SearchFragment
import com.example.weatherapp.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load the SearchFragment
        supportFragmentManager.commit {
            replace(R.id.search_fragment, SearchFragment())
        }

        // Load the HomeScreenFragment
        supportFragmentManager.commit {
            replace(R.id.home_screen_fragment_container, HomeScreenFragment())
        }

        weatherViewModel.favorites.observe(this) { favorites ->
            // Handle favorites data
        }

        weatherViewModel.loadFavorites()
    }
}