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
import com.example.weatherapp.model.FavoriteLocation
import com.example.weatherapp.view.fragments.SearchFragment
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var indicatorTabs: TabLayout
    private lateinit var loadingPage: View
    private var currentFavorites: MutableList<FavoriteLocation> = mutableListOf()

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

        viewPagerAdapter = ViewPagerAdapter(this, currentFavorites)
        viewPager.adapter = viewPagerAdapter
        setupTabLayout()
        viewPager.setCurrentItem(0, false)

        weatherViewModel.favorites.observe(this) { newFavorites ->
            updateFavorites(newFavorites)
        }

        weatherViewModel.isLoading.observe(this) { isLoading ->
            Log.d("MainActivity", "Loading updated: $isLoading")
            if (isLoading) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        }

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.search_fragment, searchFragment)
            }
        }

        weatherViewModel.setLoading(true)
        weatherViewModel.loadFavorites()
    }

    private fun showProgressBar() {
        loadingPage.visibility = View.VISIBLE
        indicatorTabs.visibility = View.GONE
    }

    private fun hideProgressBar() {
        loadingPage.visibility = View.GONE
        viewPager.visibility = View.VISIBLE
        indicatorTabs.visibility = View.VISIBLE
    }

    private fun updateFavorites(newFavorites: List<FavoriteLocation>) {
        val oldList = currentFavorites.toList()
        val oldSet = oldList.map { it.id }.toSet()
        val newSet = newFavorites.map { it.id }.toSet()

        val added = newFavorites.filter { it.id !in oldSet }
        val removed = oldList.filter { it.id !in newSet }

        for (fav in removed) {
            fav.id?.let { viewPagerAdapter.removeFavorite(it) }
        }

        for (fav in added) {
            viewPagerAdapter.addFavorite(fav)
        }

        currentFavorites.clear()
        currentFavorites.addAll(newFavorites)
    }

    private fun setupTabLayout() {
        TabLayoutMediator(indicatorTabs, viewPager) { tab, position ->
            tab.icon = AppCompatResources.getDrawable(this, R.drawable.tab_indicator)
        }.attach()
    }
}
