package com.example.movieapp.presenter.main.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.BuildConfig
import com.example.movieapp.domain.usecase.movie.GetSimilarUseCase
import com.example.movieapp.util.Constants
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SimilarMoviesViewModel @Inject constructor(
    private val getSimilarUseCase: GetSimilarUseCase
) : ViewModel() {

    fun getSimilarMovies(movieId: Int?) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val movies = getSimilarUseCase.invoke(
                BuildConfig.API_KEY,
                Constants.Movie.LANGUAGE,
                movieId = movieId
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