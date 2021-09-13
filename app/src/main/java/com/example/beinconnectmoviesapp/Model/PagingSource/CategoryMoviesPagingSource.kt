package com.example.beinconnectmoviesapp.Model.PagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.beinconnectmoviesapp.Api.MovieAPI
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.Util.Util.STARTING_INDEX
import java.lang.Exception

class CategoryMoviesPagingSource(
    private val movieAPI: MovieAPI,
    private val api: String,
    private val language: String,
    private val sort: String,
    private val withGenres: String,
    private val withoutGenres: String,
    private val originalLanguage: String,
    private val notOriginalLanguage: String,
    private val releaseDateLte: String,
    private val releaseDateGte: String,
    private val voteAverageGte: String
) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: STARTING_INDEX
        return try {
            val result = movieAPI.getMovies(
                api,
                language,
                sort,
                position,
                withGenres,
                withoutGenres,
                originalLanguage,
                notOriginalLanguage,
                releaseDateLte,
                releaseDateGte,
                voteAverageGte
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