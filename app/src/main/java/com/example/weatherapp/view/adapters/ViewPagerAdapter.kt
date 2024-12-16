package com.example.weatherapp.view.adapters
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.weatherapp.model.FavoriteLocation
import com.example.weatherapp.view.fragments.FavoriteScreenFragment
import com.example.weatherapp.view.fragments.HomeScreenFragment


class ViewPagerAdapter(
    fragmentActivity: AppCompatActivity,
    private val favorites: MutableList<FavoriteLocation>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = favorites.size + 1

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            Log.d("ViewPagerAdapter", "Loading: Creating home screen fragment")
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


    override fun getItemId(position: Int): Long {
        return if (position == 0) {
            "HOME_SCREEN".hashCode().toLong()
        } else {
            favorites[position - 1].id.hashCode().toLong()
        }
    }

    override fun containsItem(itemId: Long): Boolean {
        if (itemId == "HOME_SCREEN".hashCode().toLong()) return true
        return favorites.any { it.id.hashCode().toLong() == itemId }
    }


    fun addFavorite(favorite: FavoriteLocation) {
        favorites.add(favorite)
        notifyItemInserted(favorites.size)
    }

    fun removeFavorite(favoriteId: String) {
        val index = favorites.indexOfFirst { it.id == favoriteId }
        if (index != -1) {
            favorites.removeAt(index)
            notifyItemRemoved(index + 1) // +1 offset for home screen
        }
    }
}



