package com.example.beinconnectmoviesapp.ViewModel

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beinconnectmoviesapp.Adapter.GenresPagerAdapter
import com.example.beinconnectmoviesapp.Repo.RepositoryInterface
import com.example.beinconnectmoviesapp.Util.Resource
import com.example.beinconnectmoviesapp.Util.Util.categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repo: RepositoryInterface
) : ViewModel() {

    private val _genres = MutableLiveData<Resource<Any>>()
    val genres: LiveData<Resource<Any>> = _genres

    private val _baseMovies = MutableLiveData<Resource<Any>>()
    val baseMovies: LiveData<Resource<Any>> = _baseMovies

    private val _movieCategories = MutableLiveData<List<Resource<Any>>>()
    val movieCategories: LiveData<List<Resource<Any>>> = _movieCategories

    private var _genresPagerAdapter: GenresPagerAdapter? = null
    val genresPagerAdapter: GenresPagerAdapter?
        get() = _genresPagerAdapter

    init {
        getMovieGenres()
        getMovies()
        getCategoryResults()
    }

    fun initializePagerAdapter(fragmentManager: FragmentManager) {
        if (_genresPagerAdapter == null) {
            _genresPagerAdapter = GenresPagerAdapter(fragmentManager)
        }
    }

    private fun getMovieGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getMovieGenres()
            _genres.postValue(result)
        }
    }

    private fun getMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repo.getMovies()
            _baseMovies.postValue(result)
        }
    }

    fun getCategoryResults() {
        val movieCategoriesList = arrayListOf<Resource<Any>>()
        viewModelScope.launch(Dispatchers.IO) {
            categories.forEach {
                val categoryResult = repo.getMovies(
                    originalLanguage = it.originalLanguage,
                    notOriginalLanguage = it.notOriginalLanguage,
                    releaseDateLte = it.releaseDateLte,
                    releaseDateGte = it.releaseDateGte,
                    voteAverageGte = it.voteAverageGte
                )
                categoryResult.title = it.title
                movieCategoriesList.add(categoryResult)
            }
            _movieCategories.postValue(movieCategoriesList)
        }
    }

}