// package com.example.weatherapp.view.adapters

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.model.FavoriteLocation
import com.example.weatherapp.view.fragments.FavoriteScreenFragment
import com.example.weatherapp.view.fragments.HomeScreenFragment


class ViewPagerAdapter(
    fragmentActivity: AppCompatActivity,
    private val favorites: List<FavoriteLocation>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = favorites.size + 1

    override fun createFragment(position: Int): Fragment {
        Log.d("ViewPagerAdapter", "Creating fragment at position: $position")
        return if (position == 0) {
            HomeScreenFragment()
        } else {
            val favorite = favorites[position - 1]
            FavoriteScreenFragment().apply {
                arguments = Bundle().apply {
                    putString("city", favorite.city)
                    putString("state", favorite.state)
                    putDouble("latitude", favorite.lat)
                    putDouble("longitude", favorite.lng)
                    putString("id", favorite.id)
                }
            }
        }
    }

}



