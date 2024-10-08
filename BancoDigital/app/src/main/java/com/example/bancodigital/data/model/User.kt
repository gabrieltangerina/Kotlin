package com.example.bancodigital.data.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    var image: String = "",
    @get:Exclude
    val password: String = ""
) : Parcelable
