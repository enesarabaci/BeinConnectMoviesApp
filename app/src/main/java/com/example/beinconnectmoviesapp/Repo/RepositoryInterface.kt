package com.example.beinconnectmoviesapp.Repo

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.Util.Resource
import com.example.beinconnectmoviesapp.Util.Util.SORT_BY_POPULARITY_DESC

interface RepositoryInterface {

    suspend fun getMovieGenres() : Resource<Any>

    suspend fun getMovies(
        sort: String = SORT_BY_POPULARITY_DESC,
        page: Int = 1,
        withGenres: String = "",
        withoutGenres: String = "",
        originalLanguage: String = "",
        notOriginalLanguage: String = "",
        releaseDateLte: String = "",
        releaseDateGte: String = "",
        voteAverageGte: String = ""
    ): Resource<Any>

    fun getPagingMovies(
        sort: String = SORT_BY_POPULARITY_DESC,
        withGenres: String = "",
        withoutGenres: String = "",
        originalLanguage: String = "",
        notOriginalLanguage: String = "",
        releaseDateLte: String = "",
        releaseDateGte: String = "",
        voteAverageGte: String = ""
    ): LiveData<PagingData<Movie>>

    fun searchMovies(
        query: String
    ): LiveData<PagingData<Movie>>

}