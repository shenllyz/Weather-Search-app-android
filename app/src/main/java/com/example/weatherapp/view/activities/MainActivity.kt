package com.example.weatherapp.view.activities

import ViewPagerAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.example.weatherapp.R
import com.example.weatherapp.view.fragments.HomeScreenFragment
import com.example.weatherapp.view.fragments.ProgressBarFragment
import com.example.weatherapp.view.fragments.SearchFragment
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.weatherapp.model.FavoriteLocation
import com.google.android.material.tabs.TabLayoutMediator

// MainActivity.kt
class MainActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var indicatorTabs: TabLayout
    private val progressBarFragment = ProgressBarFragment()
    private var currentFavorites: List<FavoriteLocation> = emptyList()
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
        val progressBarFragment = ProgressBarFragment()
        val searchFragment = SearchFragment()


        val indicatorsLayout: View = findViewById(R.id.indicators)
        indicatorTabs = indicatorsLayout.findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.viewPager)
        viewPager.offscreenPageLimit = 1
        setupViewPager(emptyList())
        weatherViewModel.favorites.observe(this) { favorites ->
            Log.d("MainActivity", "Favorites updated: $favorites")
            setupViewPager(favorites)
        }

        // Add the fragments to their containers
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.search_fragment, searchFragment)
                add(R.id.progress_bar_fragment_container, progressBarFragment)

            }
        }

        // Observe isLoading LiveData
        weatherViewModel.isLoading.observe(this) { isLoading ->
            Log.d("MainActivity", "isLoading: $isLoading")
            toggleFragments(isLoading)
        }

        // Load Favorites
        weatherViewModel.loadFavorites()
        weatherViewModel.setLoading(false)
    }

    private fun toggleFragments(isLoading: Boolean) {
        supportFragmentManager.commit {
            if (isLoading) {
                show(progressBarFragment)
                viewPager.visibility = View.GONE
                indicatorTabs.visibility = View.GONE
            } else {
                hide(progressBarFragment)
                viewPager.visibility = View.VISIBLE
                indicatorTabs.visibility = View.VISIBLE
            }
        }
    }

    private fun setupViewPager(favorites: List<FavoriteLocation>) {
        if (favorites == currentFavorites) return
        currentFavorites = favorites
        viewPagerAdapter = ViewPagerAdapter(this, favorites)
        viewPager.adapter = viewPagerAdapter

        indicatorTabs.removeAllTabs()
        TabLayoutMediator(indicatorTabs, viewPager) { tab, position ->
            tab.icon = AppCompatResources.getDrawable(this, R.drawable.tab_indicator)
        }.attach()
    }
}