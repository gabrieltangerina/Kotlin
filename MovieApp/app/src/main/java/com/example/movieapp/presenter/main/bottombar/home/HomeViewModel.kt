package com.example.movieapp.presenter.main.bottombar.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.BuildConfig
import com.example.movieapp.data.mapper.toPresentation
import com.example.movieapp.domain.api.usecase.movie.GetGenresUseCase
import com.example.movieapp.domain.api.usecase.movie.GetMoviesByGenreUseCase
import com.example.movieapp.util.Constants
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase
) : ViewModel() {

    fun getGenres() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val genres = getGenresUseCase.invoke().map { it.toPresentation() }

            emit(StateView.Success(genres))

        }catch (ex: HttpException){
            ex.printStackTrace()
            emit(StateView.Error(ex.message))
        }catch (ex: Exception){
            ex.printStackTrace()
            emit(StateView.Error(ex.message))
        }
    }

    fun getMoviesByGenre(genreId: Int?) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val movies = getMoviesByGenreUseCase.invoke(
                genreId = genreId
            )

            emit(StateView.Success(movies))

        }catch (ex: HttpException){
            ex.printStackTrace()
            emit(StateView.Error(ex.message))
        }catch (ex: Exception){
            ex.printStackTrace()
            emit(StateView.Error(ex.message))
        }
    }

}