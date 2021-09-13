package com.example.beinconnectmoviesapp.Util

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

object Util {

    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    const val VIDEO_URL = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
    const val API_KEY = "API KEY MUST BE HERE"
    const val LANGUAGE = "tr-TR"
    const val STARTING_INDEX = 1

    fun toastBuilder(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    const val SORT_BY_POPULARITY_DESC = "popularity.desc"
    const val SORT_BY_RELEASE_DATE_DESC = "release_date.desc"
    const val SORT_BY_VOTE_AVERAGE_DESC = "vote_average.desc"

    fun ImageView.loadImage(url: String?) {
        val imageUrl = IMAGE_BASE_URL + url
        Glide.with(context).load(imageUrl).centerCrop().into(this)
    }

    sealed class Category(
        val title: String,
        val originalLanguage: String = "",
        val notOriginalLanguage: String = "",
        val releaseDateLte: String = "",
        val releaseDateGte: String = "",
        val voteAverageGte: String = ""
    ) {
        class Domestic(_title: String = "Yerli Filmler", _originalLanguage: String = "tr") :
            Category(title = _title, originalLanguage = _originalLanguage)

        class Foreign(_title: String = "Yabancı Filmler", _notOriginalLanguage: String = "tr") :
            Category(title = _title, notOriginalLanguage = _notOriginalLanguage)

        class Popular(_title: String = "Beğenilen Filmler", _voteAverageGte: String = "8") :
            Category(title = _title, voteAverageGte = _voteAverageGte)

        class OldTurkish(
            _title: String = "Yeşilçam Filmleri",
            _originalLanguage: String = "tr",
            _releaseDateLte: String = "1985"
        ) :
            Category(
                title = _title,
                originalLanguage = _originalLanguage,
                releaseDateLte = _releaseDateLte
            )

        class News(_title: String = "Bu Senenin Filmleri", _releaseDateGte: String = "2021") :
            Category(title = _title, releaseDateGte = _releaseDateGte)
    }

    val categories: List<Category> = listOf(
        Category.Domestic(),
        //Category.Foreign(),
        Category.Popular(),
        Category.OldTurkish(),
        Category.News()
    )

}