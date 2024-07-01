package com.example.movieapp

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class MovieDetails (
    @SerializedName("title")
    var title: String? = null,

    @SerializedName("overview")
    var overview: String? = null,

    @SerializedName("poster_path")
    var movie_image: String? = null,

    @SerializedName("movie_id")
    var movie_id: Int? = null,

    @SerializedName("vote_average")
    var vote_average: Float? = null,

    @SerializedName("vote_count")
    var vote_count: Int? = null,

    @SerializedName("release_date")
    var release_date: String? = null
) : java.io.Serializable {}
