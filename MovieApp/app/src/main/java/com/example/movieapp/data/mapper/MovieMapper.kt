package com.example.movieapp.data.mapper

import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.data.model.AuthorDetailsResponse
import com.example.movieapp.data.model.CountryResponse
import com.example.movieapp.data.model.CreditResponse
import com.example.movieapp.data.model.GenreResponse
import com.example.movieapp.data.model.MovieResponse
import com.example.movieapp.data.model.MovieReviewResponse
import com.example.movieapp.data.model.PersonResponse
import com.example.movieapp.domain.model.AuthorDetails
import com.example.movieapp.domain.model.Country
import com.example.movieapp.domain.model.Credit
import com.example.movieapp.domain.model.Genre
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieReview
import com.example.movieapp.domain.model.Person
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
        productionCountries = productionCountries?.map { it.toDomain() },
        runtime = runtime
    )
}

fun CountryResponse.toDomain(): Country {
    return Country(
        name = name
    )
}

fun Genre.toPresentation(): GenrePresentation {
    return GenrePresentation(
        id = id,
        name = name,
        movies = emptyList()
    )
}

fun PersonResponse.toDomain(): Person {
    return Person(
        adult = adult,
        gender = gender,
        id = id,
        knowForDepartment = knowForDepartment,
        name = name,
        originalName = originalName,
        popularity = popularity,
        profilePath = profilePath,
        castId = castId,
        character = character,
        creditId = creditId,
        order = order
    )
}

fun CreditResponse.toDomain(): Credit {
    return Credit(
        cast = cast?.map { it.toDomain() }
    )
}

fun AuthorDetailsResponse.toDomain(): AuthorDetails {
    return AuthorDetails(
        name = name,
        username = username,
        avatarPath = "https://image.tmdb.org/t/p/w500$avatarPath",
        rating = rating
    )
}

fun MovieReviewResponse.toDomain(): MovieReview {
    return MovieReview(
        author = author,
        authorDetails = authorDetails?.toDomain(),
        content = content,
        createdAt = createdAt,
        id = id,
        updatedAt = updatedAt,
        url = url
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        poster = posterPath,
        runtime = runtime,
        insertion = System.currentTimeMillis()
    )
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = poster,
        runtime = runtime
    )
}