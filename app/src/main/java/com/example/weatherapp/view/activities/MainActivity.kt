package com.example.weatherapp.view.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.weatherapp.R
import com.example.weatherapp.view.fragments.HomeScreenFragment
import com.example.weatherapp.view.fragments.ProgressBarFragment
import com.example.weatherapp.view.fragments.SearchFragment
import com.example.weatherapp.viewmodel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    // Fragment references
    private lateinit var progressBarFragment: ProgressBarFragment
    private lateinit var homeScreenFragment: HomeScreenFragment
    private lateinit var searchFragment: SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handle system window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Fragments
        progressBarFragment = ProgressBarFragment()
        homeScreenFragment = HomeScreenFragment()
        searchFragment = SearchFragment()

        // Add the fragments to their containers
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.search_fragment, searchFragment)
                add(R.id.progress_bar_fragment_container, progressBarFragment)
                add(R.id.home_screen_fragment_container, homeScreenFragment)
                hide(homeScreenFragment) // Initially hide HomeScreenFragment
            }
        }

        // Observe isLoading LiveData
        weatherViewModel.isLoading.observe(this) { isLoading ->
            Log.d("MainActivity", "isLoading: $isLoading")
            toggleFragments(isLoading)
        }

        // Load Favorites
        weatherViewModel.loadFavorites()
    }

    private fun toggleFragments(isLoading: Boolean) {
        supportFragmentManager.commit {
            if (isLoading) {
                show(progressBarFragment)
                hide(homeScreenFragment)
            } else {
                hide(progressBarFragment)
                show(homeScreenFragment)
            }
        }
    }
}
