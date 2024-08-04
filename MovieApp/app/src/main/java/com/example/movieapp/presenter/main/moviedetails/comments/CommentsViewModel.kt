package com.example.movieapp.presenter.main.moviedetails.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.BuildConfig
import com.example.movieapp.domain.api.usecase.movie.GetMovieReviewsUseCase
import com.example.movieapp.util.Constants
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase
) : ViewModel() {

    fun getMovieReviews(movieId: Int?) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val movies = getMovieReviewsUseCase.invoke(movieId = movieId)

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