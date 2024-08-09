package com.example.movieapp.presenter.main.moviedetails.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.domain.api.usecase.movie.GetCreditsUseCase
import com.example.movieapp.domain.api.usecase.movie.GetMovieDetailsUseCase
import com.example.movieapp.domain.local.usecase.InsertMovieUseCase
import com.example.movieapp.domain.model.movie.Movie
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getCreditsUseCase: GetCreditsUseCase,
    private val insertMovieUseCase: InsertMovieUseCase
) : ViewModel() {

    private val _movieId = MutableLiveData(0)
    val movieId: LiveData<Int> = _movieId

    fun getMovieDetails(movieId: Int?) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val details = getMovieDetailsUseCase.invoke(movieId = movieId)

            emit(StateView.Success(details))

        } catch (ex: HttpException) {
            ex.printStackTrace()
            emit(StateView.Error(ex.message))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(StateView.Error(ex.message))
        }
    }

    fun getCredits(movieId: Int?) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val credits = getCreditsUseCase.invoke(movieId = movieId)

            emit(StateView.Success(credits))

        } catch (ex: HttpException) {
            ex.printStackTrace()
            emit(StateView.Error(ex.message))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(StateView.Error(ex.message))
        }
    }

    fun insertMovie(movie: Movie) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            insertMovieUseCase.invoke(movie)

            emit(StateView.Success(Unit))

        } catch (ex: Exception) {
            ex.printStackTrace()
            emit(StateView.Error(ex.message))
        }
    }

    fun setMovieId(movieId: Int) {
        _movieId.value = movieId
    }

}