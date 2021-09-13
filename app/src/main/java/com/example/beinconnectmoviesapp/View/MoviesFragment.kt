package com.example.beinconnectmoviesapp.View

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.beinconnectmoviesapp.Adapter.MoviesCategoriesAdapter
import com.example.beinconnectmoviesapp.Adapter.MoviesGenresAdapter
import com.example.beinconnectmoviesapp.Adapter.MoviesPagerAdapter
import com.example.beinconnectmoviesapp.Model.GenreX
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.R
import com.example.beinconnectmoviesapp.Util.Resource
import com.example.beinconnectmoviesapp.Util.Util.toastBuilder
import com.example.beinconnectmoviesapp.ViewModel.MoviesViewModel
import com.example.beinconnectmoviesapp.databinding.FragmentMoviesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.fragment_movies) {

    private lateinit var binding: FragmentMoviesBinding
    private val viewPagerAdapter = MoviesPagerAdapter()
    private val genresAdapter = MoviesGenresAdapter()
    private val categoriesAdapter = MoviesCategoriesAdapter()
    private val viewModel: MoviesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMoviesBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.fragmentMoviesToolbar)
            supportActionBar?.setTitle("")
        }
        setHasOptionsMenu(true)

        binding.fragmentMoviesSrl.apply {
            setOnRefreshListener { viewModel.getCategoryResults() }
        }

        println("position on create")

        observeData()
        setupRecyclerView()
        setupViewPager()
    }

    private fun observeData() {
        viewModel.apply {
            baseMovies.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val list = resource.data as List<Movie>
                        viewPagerAdapter.updateImageList(list)
                    }
                    is Resource.Error -> {
                        toastBuilder(resource.message!!, requireContext())
                    }
                }
            }
            genres.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val list = resource.data as List<GenreX>
                        genresAdapter.updateGenreList(list)
                    }
                    is Resource.Error -> {
                        toastBuilder(resource.message!!, requireContext())
                    }
                }
            }
            movieCategories.observe(viewLifecycleOwner) { list ->
                binding.fragmentMoviesSrl.isRefreshing = false
                categoriesAdapter.list = list
            }
        }
    }

    private fun setupViewPager() {
        binding.fragmentMoviesViewpager.apply {
            adapter = viewPagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }
        viewPagerAdapter.setOnMovieClickListener { movie ->
            val intentToPlayer = Intent(activity, PlayerActivity::class.java).also {
                it.putExtra("movie", movie)
            }
            startActivity(intentToPlayer)
        }
        viewPagerAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                binding.fragmentMoviesIndicator.setViewPager(binding.fragmentMoviesViewpager)
                super.onChanged()
            }
        })
    }

    private fun setupRecyclerView() {
        binding.fragmentMoviesGenresRv.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = genresAdapter
        }
        genresAdapter.setOnGenreClickListener { genrex ->
            findNavController().navigate(MoviesFragmentDirections.actionMoviesFragmentToGenresFragment(genrex.id))
        }

        binding.fragmentMoviesRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoriesAdapter
        }
        categoriesAdapter.apply {
            setOnCategoryClickListener { categoryTitle ->
                findNavController().navigate(MoviesFragmentDirections.actionMoviesFragmentToCategoryFragment(categoryTitle))
            }
            setOnMovieClickListener { movie ->
                val intentToPlayer = Intent(activity, PlayerActivity::class.java).also {
                    it.putExtra("movie", movie)
                }
                startActivity(intentToPlayer)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            findNavController().navigate(MoviesFragmentDirections.actionGlobalSearchFragment())
        }

        return super.onOptionsItemSelected(item)
    }

}