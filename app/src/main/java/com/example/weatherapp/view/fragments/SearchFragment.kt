package com.example.weatherapp.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.weatherapp.R
import com.example.weatherapp.view.activities.SearchableActivity
import com.example.weatherapp.viewmodel.WeatherViewModel

class SearchFragment : Fragment() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_bar, container, false)

        val appTitle: TextView = view.findViewById(R.id.app_title)
        val searchIcon: ImageView = view.findViewById(R.id.searchicon)
        val searchable: SearchView = view.findViewById(R.id.searchable)
        val backicon: ImageView = view.findViewById(R.id.close_search)
        val deleteSearch: ImageView = view.findViewById(R.id.delete_search)

        searchable.isEnabled = false
        searchable.isClickable = false
        searchable.isFocusable = false
        backicon.visibility = View.GONE
        deleteSearch.visibility = View.GONE

        val searchEditText: EditText = searchable.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(resources.getColor(android.R.color.white, null))

        searchIcon.setOnClickListener {
            searchable.isEnabled = true
            searchable.isClickable = true
            searchable.isFocusable = true
            searchable.isIconified = false
            searchable.requestFocus()
            deleteSearch.visibility = View.VISIBLE
            appTitle.visibility = View.GONE
            searchIcon.visibility = View.GONE
            backicon.visibility = View.VISIBLE
        }

        backicon.setOnClickListener {
            searchable.isEnabled = false
            searchable.isClickable = false
            searchable.isFocusable = false
            searchable.isIconified = true
            appTitle.visibility = View.VISIBLE
            searchIcon.visibility = View.VISIBLE
            deleteSearch.visibility = View.GONE
            backicon.visibility = View.GONE
        }

        deleteSearch.setOnClickListener {
            searchEditText.text.clear()
        }

        searchable.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    weatherViewModel.loadGeocodingData(it)
                    weatherViewModel.latitude.observe(viewLifecycleOwner) { lat ->
                        weatherViewModel.longitude.observe(viewLifecycleOwner) { lon ->
                            weatherViewModel.loadWeatherData(lat, lon)
                            weatherViewModel.formattedAddress.observe(viewLifecycleOwner) { address ->
                                val intent = Intent(activity, SearchableActivity::class.java).apply {
                                    putExtra("formatted_address", address)
                                }
                                startActivity(intent)
                            }
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return view
    }
}