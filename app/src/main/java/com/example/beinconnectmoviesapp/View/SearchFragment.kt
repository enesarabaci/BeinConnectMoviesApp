package com.example.beinconnectmoviesapp.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.beinconnectmoviesapp.Adapter.MovieLoadStateAdapter
import com.example.beinconnectmoviesapp.Adapter.MoviesPagingAdapter
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.R
import com.example.beinconnectmoviesapp.Util.Resource
import com.example.beinconnectmoviesapp.ViewModel.SearchViewModel
import com.example.beinconnectmoviesapp.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private val moviesAdapter = MoviesPagingAdapter()
    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.fragmentSearchToolbar)
            setupActionBarWithNavController(findNavController())
        }
        binding.fragmentSearchToolbar.setTitle("")
        setHasOptionsMenu(true)

        binding.fragmentSearchEditText.doOnTextChanged { text, start, before, count ->
            job?.cancel()
            if (text.toString().length < 3) {
                viewModel.clearData()
            } else {
                job = CoroutineScope(Dispatchers.Main).launch {
                    delay(1000)
                    viewModel.searchMovie(text.toString())
                }
            }
        }

        observeData()
        setupRecyclerView()
    }

    private fun observeData() {
        viewModel.data.observe(viewLifecycleOwner) { resource ->
            binding.apply {
                when (resource) {
                    is Resource.Loading -> {
                        fragmentSearchPb.isVisible = true
                    }
                    is Resource.Empty -> {
                        fragmentSearchPb.isVisible = false
                        moviesAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
                    }
                }
            }
        }
        viewModel.result?.observe(viewLifecycleOwner) { pagingData: PagingData<Movie> ->
            binding.fragmentSearchPb.isVisible = false
            moviesAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
    }

    private fun setupRecyclerView() {
        binding.fragmentSearchRv.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = moviesAdapter.withLoadStateHeaderAndFooter(
                header = MovieLoadStateAdapter{ moviesAdapter.retry() },
                footer = MovieLoadStateAdapter{ moviesAdapter.retry() }
            )
        }
        moviesAdapter.setOnMovieClickListener { movie ->
            val intentToPlayer = Intent(activity, PlayerActivity::class.java).also {
                it.putExtra("movie", movie)
            }
            startActivity(intentToPlayer)
        }
    }

}