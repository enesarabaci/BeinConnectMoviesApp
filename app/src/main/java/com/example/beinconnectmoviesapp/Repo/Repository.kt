package com.example.beinconnectmoviesapp.Repo

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.beinconnectmoviesapp.Api.MovieAPI
import com.example.beinconnectmoviesapp.Model.PagingSource.CategoryMoviesPagingSource
import com.example.beinconnectmoviesapp.Model.Movie
import com.example.beinconnectmoviesapp.Model.PagingSource.SearchMoviesPagingSource
import com.example.beinconnectmoviesapp.Util.Resource
import com.example.beinconnectmoviesapp.Util.Util.API_KEY
import com.example.beinconnectmoviesapp.Util.Util.LANGUAGE
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: MovieAPI
) : RepositoryInterface {

    override suspend fun getMovieGenres(): Resource<Any> {
        return tryCatchBuilder {
            val genre = api.getMovieGenres(
                API_KEY,
                LANGUAGE
            )
            Resource.Success(genre.genres)
        }
    }

    override suspend fun getMovies(
        sort: String,
        page: Int,
        withGenres: String,
        withoutGenres: String,
        originalLanguage: String,
        notOriginalLanguage: String,
        releaseDateLte: String,
        releaseDateGte: String,
        voteAverageGte: String
    ): Resource<Any> {
        return tryCatchBuilder {
            val result = api.getMovies(
                API_KEY,
                LANGUAGE,
                sort = sort,
                page = page,
                withGenres = withGenres,
                withoutGenres = withoutGenres,
                originalLanguage = originalLanguage,
                notOriginalLanguage = notOriginalLanguage,
                releaseDateLte = releaseDateLte,
                releaseDateGte = releaseDateGte,
                voteAverageGte = voteAverageGte
            )
            Resource.Success(result.results)
        }
    }

    override fun getPagingMovies(
        sort: String,
        withGenres: String,
        withoutGenres: String,
        originalLanguage: String,
        notOriginalLanguage: String,
        releaseDateLte: String,
        releaseDateGte: String,
        voteAverageGte: String
    ): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                CategoryMoviesPagingSource(
                    api,
                    API_KEY,
                    LANGUAGE,
                    sort,
                    withGenres,
                    withoutGenres,
                    originalLanguage,
                    notOriginalLanguage,
                    releaseDateLte,
                    releaseDateGte,
                    voteAverageGte
                )
            }
        ).liveData
    }

    override fun searchMovies(query: String): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchMoviesPagingSource(
                    api,
                    API_KEY,
                    LANGUAGE,
                    query
                )
            }
        ).liveData
    }

    private suspend fun <T> tryCatchBuilder(task: suspend (() -> Resource<T>)): Resource<T> {
        return try {
            task.invoke()
        } catch (e: HttpException) {
            Resource.Error("Beklenmedik bir hata oluştu.")
        } catch (e: IOException) {
            Resource.Error("Sunucuya ulaşılamadı. İnternet bağlantınızı kontrol edin.")
        }
    }

}