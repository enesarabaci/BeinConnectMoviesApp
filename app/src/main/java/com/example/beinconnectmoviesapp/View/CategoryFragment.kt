package com.example.beinconnectmoviesapp.View

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.beinconnectmoviesapp.Adapter.MovieLoadStateAdapter
import com.example.beinconnectmoviesapp.Adapter.MoviesPagingAdapter
import com.example.beinconnectmoviesapp.Model.GenreX
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.R
import com.example.beinconnectmoviesapp.Util.Util.SORT_BY_POPULARITY_DESC
import com.example.beinconnectmoviesapp.Util.Util.SORT_BY_RELEASE_DATE_DESC
import com.example.beinconnectmoviesapp.Util.Util.SORT_BY_VOTE_AVERAGE_DESC
import com.example.beinconnectmoviesapp.ViewModel.CategoryViewModel
import com.example.beinconnectmoviesapp.databinding.FragmentCategoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var binding: FragmentCategoryBinding
    private val moviesAdapter = MoviesPagingAdapter()
    private val viewModel: CategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCategoryBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        val title = arguments?.getString("categoryTitle")
        val secondTitle = arguments?.getSerializable("secondCategoryTitle") as GenreX?
        binding.fragmentCategoryTitle.setText("$title ${if (secondTitle != null) "- ${secondTitle.name}" else ""}")

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.fragmentCategoryToolbar)
            setupActionBarWithNavController(findNavController())
        }
        binding.fragmentCategoryToolbar.setTitle("")
        setHasOptionsMenu(true)

        binding.fragmentCategorySort.setOnClickListener {
            val popup = PopupMenu(requireContext(), it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.sort_menu, popup.menu)
            when (viewModel.getSortData()) {
                SORT_BY_POPULARITY_DESC -> popup.menu.findItem(R.id.popularity).setChecked(true)
                SORT_BY_VOTE_AVERAGE_DESC -> popup.menu.findItem(R.id.vote_average).setChecked(true)
                SORT_BY_RELEASE_DATE_DESC -> popup.menu.findItem(R.id.release_date).setChecked(true)
            }
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.popularity -> viewModel.updateSort(SORT_BY_POPULARITY_DESC)
                    R.id.vote_average -> viewModel.updateSort(SORT_BY_VOTE_AVERAGE_DESC)
                    R.id.release_date -> viewModel.updateSort(SORT_BY_RELEASE_DATE_DESC)
                }
                true
            }
            popup.show()
        }

        observeData()
        setupRv()
    }

    private fun observeData() {
        viewModel.deneme.observe(viewLifecycleOwner) { pagingData: PagingData<Movie> ->
            binding.fragmentCategoryPb.isVisible = false
            moviesAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
    }

    private fun setupRv() {
        binding.fragmentCategoryRv.apply {
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            findNavController().navigate(CategoryFragmentDirections.actionGlobalSearchFragment())
        }
        return super.onOptionsItemSelected(item)
    }

}