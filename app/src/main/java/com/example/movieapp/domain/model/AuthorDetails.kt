package com.example.movieapp.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthorDetails(

    val name: String?,

    val username: String?,

    val avatarPath: String?,

    val rating: Int?

) : Parcelable
