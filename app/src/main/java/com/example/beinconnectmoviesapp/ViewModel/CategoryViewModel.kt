package com.example.beinconnectmoviesapp.ViewModel

import androidx.lifecycle.*
import com.example.beinconnectmoviesapp.Model.GenreX
import com.example.beinconnectmoviesapp.Repo.RepositoryInterface
import com.example.beinconnectmoviesapp.Util.Resource
import com.example.beinconnectmoviesapp.Util.Util.SORT_BY_POPULARITY_DESC
import com.example.beinconnectmoviesapp.Util.Util.categories
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repo: RepositoryInterface,
    state: SavedStateHandle
) : ViewModel() {

    private val sort = state.getLiveData("sort_type", SORT_BY_POPULARITY_DESC)
    private val title = state.getLiveData<String>("categoryTitle")
    private val genre = state.getLiveData<GenreX>("secondCategoryTitle")

    private val _data = MutableLiveData<Resource<Any>>()
    val data: LiveData<Resource<Any>> = _data

    val deneme = sort.switchMap {
        val categories = categories.filter { it.title == title.value }
        val category = categories.first()
        repo.getPagingMovies(
            sort = it,
            withGenres = if (genre.value?.id != null) genre.value?.id.toString() else "",
            originalLanguage = category.originalLanguage,
            notOriginalLanguage = category.notOriginalLanguage,
            releaseDateLte = category.releaseDateLte,
            releaseDateGte = category.releaseDateGte,
            voteAverageGte = category.voteAverageGte
        )
    }

    fun updateSort(sortType: String) {
        sort.value = sortType
    }

    fun getSortData(): String {
        return sort.value ?: SORT_BY_POPULARITY_DESC
    }

}