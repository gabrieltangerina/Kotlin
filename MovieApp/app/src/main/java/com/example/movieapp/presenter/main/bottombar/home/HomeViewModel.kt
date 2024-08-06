package com.example.movieapp.presenter.main.bottombar.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.api.usecase.movie.GetGenresUseCase
import com.example.movieapp.domain.api.usecase.movie.GetMoviesByGenreUseCase
import com.example.movieapp.domain.model.Genre
import com.example.movieapp.presenter.model.MoviesByGenre
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase
) : ViewModel() {

    private val _movieList = MutableLiveData<List<MoviesByGenre>>()
    val movieList: LiveData<List<MoviesByGenre>>
        get() = _movieList

    private val _homeState = MutableLiveData<StateView<Unit>>()
    val homeState: LiveData<StateView<Unit>> get() = _homeState

    init {
        getGenres()
    }

    private fun getGenres() {
        viewModelScope.launch {
            try {
                _homeState.postValue(StateView.Loading())

                val genres = getGenresUseCase()
                getMoviesByGenre(genres)

            } catch (e: Exception) {
                e.printStackTrace()
                _homeState.postValue(StateView.Error(e.message))
            }
        }
    }

    private fun getMoviesByGenre(genres: List<Genre>){
        val moviesByGenre : MutableList<MoviesByGenre> = mutableListOf()
        viewModelScope.launch{
            genres.forEach { genre ->
                try {

                    val movies = getMoviesByGenreUseCase(genreId = genre.id)
                    val movieByGenre = MoviesByGenre(
                        id = genre.id,
                        name = genre.name,
                        movies = movies.map { it.toDomain() }.take(5)
                    )

                    moviesByGenre.add(movieByGenre)

                    if(moviesByGenre.size == genres.size){
                        _movieList.postValue(moviesByGenre)
                        _homeState.postValue(StateView.Success(Unit))
                    }

                }catch (e: Exception){
                    e.printStackTrace()
                    _homeState.postValue(StateView.Error(e.message))
                }
            }
        }
    }

}