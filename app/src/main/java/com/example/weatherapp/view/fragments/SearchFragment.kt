package com.example.weatherapp.view.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.R
import com.example.weatherapp.view.activities.SearchableActivity
import com.example.weatherapp.viewmodel.WeatherViewModel

class SearchFragment : Fragment() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private var isActivityStarted = false
    private lateinit var citySugAdapter: ArrayAdapter<String>
    private var beforeSearchText = ""

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_bar, container, false)

        val appTitle: TextView = view.findViewById(R.id.app_title)
        val searchIcon: ImageView = view.findViewById(R.id.searchicon)
        val searchable: SearchView = view.findViewById(R.id.searchable)
        val backicon: ImageView = view.findViewById(R.id.close_search)


        // Initial state configuration
        searchable.isEnabled = false
        searchable.isClickable = false
        searchable.isFocusable = false
        backicon.visibility = View.GONE


        // Directly reference the search_src_text
        val searchEditText: SearchView.SearchAutoComplete =
            searchable.findViewById(androidx.appcompat.R.id.search_src_text)

        searchEditText.apply {
            inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            setTextColor(resources.getColor(android.R.color.white, null))
            setHintTextColor(resources.getColor(android.R.color.darker_gray, null))
        }

        // Initialize ArrayAdapter
        citySugAdapter = ArrayAdapter(requireContext(), R.layout.autocomplete_dropdown, mutableListOf())
        searchEditText.setAdapter(citySugAdapter)
        searchEditText.threshold = 1 // Start suggesting after 1 character

        // Listen for search icon click events
        searchIcon.setOnClickListener {
            searchable.isEnabled = true
            searchable.isClickable = true
            searchable.isFocusable = true
            searchable.visibility = View.VISIBLE
            searchable.isIconified = false
            searchable.requestFocus()

            appTitle.visibility = View.GONE
            searchIcon.visibility = View.GONE
            backicon.visibility = View.VISIBLE
        }

        // Listen for close search button click events
        backicon.setOnClickListener {
            searchable.isEnabled = false
            searchable.isClickable = false
            searchable.isFocusable = false
            searchable.isIconified = true
            searchable.visibility = View.GONE
            appTitle.visibility = View.VISIBLE
            searchIcon.visibility = View.VISIBLE

            backicon.visibility = View.GONE
        }



        // Listen for text changes
        searchEditText.addTextChangedListener {
            val currentText = it.toString()
            if (beforeSearchText != currentText) {
                beforeSearchText = currentText
                weatherViewModel.loadCityAutocomplete(currentText)
            }
        }

        // Observe city autocomplete suggestions from ViewModel
        weatherViewModel.cityAutocomplete.observe(viewLifecycleOwner) { locSuggestions ->
            val formattedSuggestions = locSuggestions.map { "${it.first}, ${it.second}" }
            citySugAdapter.clear()
            citySugAdapter.addAll(formattedSuggestions)
            searchEditText.setText(searchEditText.text)
            searchEditText.setSelection(searchEditText.text.length)
        }

        // Listen for search submit events
        searchable.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!isActivityStarted && !query.isNullOrBlank()) {
                    isActivityStarted = true
                    weatherViewModel.loadGeocodingData(query)
                    // Observe data changes in ViewModel
                    weatherViewModel.latitude.observe(viewLifecycleOwner) { lat ->
                        weatherViewModel.longitude.observe(viewLifecycleOwner) { lon ->
                            weatherViewModel.formattedAddress.observe(viewLifecycleOwner) { address ->
                                val intent = Intent(activity, SearchableActivity::class.java).apply {
                                    putExtra("formatted_address", address)
                                    putExtra("latitude", lat)
                                    putExtra("longitude", lon)
                                    putExtra("city_name", query)
                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                startActivity(intent)
                                isActivityStarted = false
                            }
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // No need to handle this as we already listen to text changes via TextWatcher
                return false
            }
        })

        // Listen for autocomplete suggestion item click events
        searchEditText.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            // Submit search
            searchable.setQuery(selectedItem, true)
        }

        return view
    }
}