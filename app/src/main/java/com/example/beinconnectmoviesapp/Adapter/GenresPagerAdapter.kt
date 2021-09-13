package com.example.beinconnectmoviesapp.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.beinconnectmoviesapp.Model.GenreX
import com.example.beinconnectmoviesapp.View.GenreFragment

class GenresPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val genres = ArrayList<GenreX>()

    override fun getCount(): Int {
        return genres.size
    }

    override fun getItem(position: Int): Fragment {
        return GenreFragment(genres.get(position))
    }

    override fun getPageTitle(position: Int): CharSequence {
        return genres.get(position).name
    }

    fun updateList(list: List<GenreX>) {
        genres.clear()
        genres.addAll(list)
        notifyDataSetChanged()
    }

}