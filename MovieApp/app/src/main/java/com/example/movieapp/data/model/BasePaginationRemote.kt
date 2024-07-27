package com.example.movieapp.data.model

import com.google.gson.annotations.SerializedName

data class BasePaginationRemote<out T>(
    @SerializedName("page")
    val page: Int?,

    @SerializedName("results")
    val results: T?,

    @SerializedName("total_pages")
    val totalPages: Int?,

    @SerializedName("total_results")
    val totalResults: Int?
)
