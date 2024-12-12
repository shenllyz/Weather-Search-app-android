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
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var loadingPage: View
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


        val searchFragment = SearchFragment()

        loadingPage = findViewById(R.id.loading_page)
        val indicatorsLayout: View = findViewById(R.id.indicators)
        indicatorTabs = indicatorsLayout.findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.viewPager)
        viewPager.offscreenPageLimit = 1
        setupViewPager(emptyList())
        weatherViewModel.favorites.observe(this) { favorites ->
            Log.d("MainActivity", "Favorites updated: $favorites")
            setupViewPager(favorites)
        }
        weatherViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showProgressBar()
            }
            else {
                hideProgressBarAndShowViewPager()
            }
        }

        // Add the fragments to their containers
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.search_fragment, searchFragment)
            }
        }
        // Load Favorites
        weatherViewModel.loadFavorites()
    }
    private fun showProgressBar() {
        loadingPage.visibility = View.VISIBLE
        viewPager.visibility = View.GONE
        indicatorTabs.visibility = View.GONE
    }

    private fun hideProgressBarAndShowViewPager() {
            loadingPage.visibility = View.GONE
            viewPager.visibility = View.VISIBLE
            indicatorTabs.visibility = View.VISIBLE
    }


    private fun setupViewPager(favorites: List<FavoriteLocation>) {
        if (favorites == currentFavorites) return
        currentFavorites = favorites
        viewPagerAdapter = ViewPagerAdapter(this, favorites)
        viewPager.adapter = viewPagerAdapter


        viewPager.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                Log.d("MainActivity", "ViewPager data loaded")
                hideProgressBarAndShowViewPager()
            }
        })

        indicatorTabs.removeAllTabs()
        TabLayoutMediator(indicatorTabs, viewPager) { tab, position ->
            tab.icon = AppCompatResources.getDrawable(this, R.drawable.tab_indicator)
        }.attach()

    }
}