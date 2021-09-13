package com.example.beinconnectmoviesapp.Model.PagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.beinconnectmoviesapp.Api.MovieAPI
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.Util.Util.STARTING_INDEX
import java.lang.Exception

class SearchMoviesPagingSource(
    private val movieAPI: MovieAPI,
    private val api: String,
    private val language: String,
    private val query: String
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: STARTING_INDEX
        return try {
            val result = movieAPI.searchMovies(
                api,
                language,
                position,
                query
            )
            LoadResult.Page(
                data = result.results,
                prevKey = if (position == STARTING_INDEX) null else position-1,
                nextKey = if (result.results.isEmpty()) null else position+1
            )
        }
        catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}