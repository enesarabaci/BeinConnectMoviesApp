package com.example.beinconnectmoviesapp.Model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Movie(
    val id: Int,
    @SerializedName("genre_ids")
    val genres: List<Int>,
    @SerializedName("poster_path")
    val image: String,
    @SerializedName("original_title")
    val title: String
) : Serializable
