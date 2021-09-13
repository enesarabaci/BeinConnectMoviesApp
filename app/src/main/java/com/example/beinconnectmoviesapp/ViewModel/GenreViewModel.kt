package com.example.beinconnectmoviesapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beinconnectmoviesapp.Repo.RepositoryInterface
import com.example.beinconnectmoviesapp.Util.Resource
import com.example.beinconnectmoviesapp.Util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val repo: RepositoryInterface
) : ViewModel() {

    private val _movieCategories = MutableLiveData<Resource<Any>>()
    val movieCategories: LiveData<Resource<Any>> = _movieCategories

    fun getCategoryResults(genreId: Int) {
        val movieCategoriesList = arrayListOf<Resource<Any>>()
        _movieCategories.postValue(Resource.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            Util.categories.forEach {
                val categoryResult = repo.getMovies(
                    withGenres = genreId.toString(),
                    originalLanguage = it.originalLanguage,
                    notOriginalLanguage = it.notOriginalLanguage,
                    releaseDateLte = it.releaseDateLte,
                    releaseDateGte = it.releaseDateGte,
                    voteAverageGte = it.voteAverageGte
                )
                categoryResult.title = it.title
                movieCategoriesList.add(categoryResult)
            }
            _movieCategories.postValue(Resource.Success(movieCategoriesList))
        }
    }

}