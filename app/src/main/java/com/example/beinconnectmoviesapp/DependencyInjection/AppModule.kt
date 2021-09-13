package com.example.beinconnectmoviesapp.DependencyInjection

import com.example.beinconnectmoviesapp.Api.MovieAPI
import com.example.beinconnectmoviesapp.Repo.Repository
import com.example.beinconnectmoviesapp.Repo.RepositoryInterface
import com.example.beinconnectmoviesapp.Util.Util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getMovieAPI(): MovieAPI =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(MovieAPI::class.java)

    @Singleton
    @Provides
    fun getRepositoryInterface(api: MovieAPI): RepositoryInterface =
        Repository(api)

}