package com.example.movieapp.data.mapper

import com.example.movieapp.data.model.CountryResponse
import com.example.movieapp.data.model.GenreResponse
import com.example.movieapp.data.model.MovieResponse
import com.example.movieapp.domain.model.Country
import com.example.movieapp.domain.model.Genre
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presenter.model.GenrePresentation

fun GenreResponse.toDomain(): Genre {
    return Genre(
        id = id,
        name = name
    )
}

fun MovieResponse.toDomain(): Movie {
    return Movie(
        adult = adult,
        backdropPath = backdropPath,
        genres = genres?.map { it.toDomain() },
        id = id,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        productionCountries = productionCountries?.map { it.toDomain() }
    )
}

fun CountryResponse.toDomain(): Country{
    return Country(
        name = name
    )
}

fun Genre.toPresentation() : GenrePresentation{
    return GenrePresentation(
        id = id,
        name = name,
        movies = emptyList()
    )
}

