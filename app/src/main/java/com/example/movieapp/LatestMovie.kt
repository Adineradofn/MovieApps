package com.example.movieapp

import com.google.gson.annotations.SerializedName

class LatestMovie {
    @JvmField
    @SerializedName("title")
    var title: String? = null

    @SerializedName("overview")
    var overview: String? = null

    @SerializedName("poster_path")
    var movie_image: String? = null

    @SerializedName("id")
    var id: Int? = null
}