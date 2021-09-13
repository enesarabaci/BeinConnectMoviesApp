package com.example.beinconnectmoviesapp.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beinconnectmoviesapp.Adapter.MoviesCategoriesAdapter
import com.example.beinconnectmoviesapp.Model.GenreX
import com.example.beinconnectmoviesapp.R
import com.example.beinconnectmoviesapp.Util.Resource
import com.example.beinconnectmoviesapp.ViewModel.GenreViewModel
import com.example.beinconnectmoviesapp.databinding.FragmentGenreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenreFragment(
    private val genreX: GenreX
) : Fragment(R.layout.fragment_genre) {

    private lateinit var binding: FragmentGenreBinding
    private val viewModel: GenreViewModel by viewModels()
    private val categoriesAdapter = MoviesCategoriesAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentGenreBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        viewModel.getCategoryResults(genreX.id)
        observeData()
        setupRecyclerView()

        super.onResume()
    }

    private fun observeData() {
        viewModel.movieCategories.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.fragmentGenrePb.isVisible = false
                    val list: ArrayList<Resource<Any>> = resource.data as ArrayList<Resource<Any>>
                    categoriesAdapter.list = list
                }
                is Resource.Loading -> {
                    binding.fragmentGenrePb.isVisible = true
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.fragmentGenreRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoriesAdapter
        }
        categoriesAdapter.apply {
            setOnCategoryClickListener { categoryTitle ->
                findNavController().navigate(GenresFragmentDirections.actionGenresFragmentToCategoryFragment(categoryTitle, genreX))
            }
            setOnMovieClickListener { movie ->
                val intentToPlayer = Intent(activity, PlayerActivity::class.java).also {
                    it.putExtra("movie", movie)
                }
                startActivity(intentToPlayer)
            }
        }
    }

}