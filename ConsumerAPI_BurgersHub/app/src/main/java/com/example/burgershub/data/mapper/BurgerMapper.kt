package com.example.burgershub.data.mapper

import com.example.burgershub.data.model.BurgerResponse
import com.example.burgershub.data.model.ImageResponse
import com.example.burgershub.data.model.IngredientResponse
import com.example.burgershub.domain.model.Burger
import com.example.burgershub.domain.model.Image
import com.example.burgershub.domain.model.Ingredient

/*
* Transforma o objeto BurgerResponse da camada de data para burger da camada de domain
* obs -> o this se refere ao objeto do tipo BurgerResponse que chama o toDomain()
*/
fun BurgerResponse.toDomain() = Burger(
    desc = this.desc,
    id = this.id,
    image = this.imageResponses?.map { it?.toDomain() },
    ingredient =  this.ingredientResponses?.map { it?.toDomain() },
    name = this.name,
    price = this.price,
    veg = this.veg
)

fun ImageResponse.toDomain() = Image(
    lg = this.lg,
    sm = this.sm
)

fun IngredientResponse.toDomain() = Ingredient(
    id = this.id,
    img = this.img,
    name = this.name
)