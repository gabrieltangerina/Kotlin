package com.example.burgershub.domain.model

data class Burger(
    val desc: String?,
    val id: Int?,
    val image: List<Image?>?,
    val ingredient: List<Ingredient?>?,
    val name: String?,
    val price: Float?,
    val veg: Boolean?
)