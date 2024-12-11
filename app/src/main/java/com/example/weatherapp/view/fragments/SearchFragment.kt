package com.example.weatherapp.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import com.example.weatherapp.R

class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_search, container, false)

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
        return view
    }
}