package com.example.beinconnectmoviesapp.View

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.beinconnectmoviesapp.Model.GenreX
import com.example.beinconnectmoviesapp.R
import com.example.beinconnectmoviesapp.Util.Resource
import com.example.beinconnectmoviesapp.Util.Util
import com.example.beinconnectmoviesapp.ViewModel.MoviesViewModel
import com.example.beinconnectmoviesapp.databinding.FragmentGenresBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenresFragment : Fragment(R.layout.fragment_genres) {

    private lateinit var binding: FragmentGenresBinding
    private val viewModel: MoviesViewModel by viewModels()
    private var genreId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentGenresBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        genreId = arguments?.getInt("genreId")

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.fragmentGenresToolbar)
            setupActionBarWithNavController(findNavController())
        }
        binding.fragmentGenresToolbar.setTitle("")
        setHasOptionsMenu(true)

        viewModel.initializePagerAdapter(childFragmentManager)

        setupViewPager()
        observeData()
    }

    private fun observeData() {
        viewModel.genres.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    val list = resource.data as List<GenreX>
                    if (viewModel.genresPagerAdapter?.genres?.toList() != list) {
                        viewModel.genresPagerAdapter?.updateList(list)
                        setupViewPager()
                    }
                }
                is Resource.Error -> {
                    Util.toastBuilder(resource.message!!, requireContext())
                }
            }
        }
    }

    private fun setupViewPager() {
        binding.apply {
            fragmentGenresViewpager.setAdapter(viewModel.genresPagerAdapter)

            val genres = viewModel.genresPagerAdapter?.genres
            var position = 0
            if (!genres.isNullOrEmpty()) {
                val item = genres.filter { it.id == genreId }.first()
                position = genres.indexOf(item)
            }
            fragmentGenresTablayout.setupWithViewPager(fragmentGenresViewpager)
            fragmentGenresViewpager.setCurrentItem(position)
            fragmentGenresViewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(fragmentGenresTablayout))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            findNavController().navigate(GenresFragmentDirections.actionGlobalSearchFragment())
        }
        return super.onOptionsItemSelected(item)
    }

}