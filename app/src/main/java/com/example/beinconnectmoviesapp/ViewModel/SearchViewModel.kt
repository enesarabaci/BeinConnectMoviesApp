package com.example.beinconnectmoviesapp.ViewModel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.Repo.RepositoryInterface
import com.example.beinconnectmoviesapp.Util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: RepositoryInterface,
    state: SavedStateHandle
) : ViewModel() {

    private val _data = MutableLiveData<Resource<Any>>()
    val data: LiveData<Resource<Any>> = _data

    private val query = state.getLiveData<String>("query")

    var result: LiveData<PagingData<Movie>>? = query.switchMap {
        repo.searchMovies(it).cachedIn(viewModelScope)
    }

    fun searchMovie(q: String) {
        result = null
        _data.value = Resource.Loading
        query.value = q
    }

    fun clearData() {
        result = null
        _data.value = Resource.Empty
    }

}