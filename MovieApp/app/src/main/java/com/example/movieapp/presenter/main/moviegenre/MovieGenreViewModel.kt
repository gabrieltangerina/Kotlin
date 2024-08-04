package com.example.movieapp.presenter.main.moviegenre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.BuildConfig
import com.example.movieapp.domain.api.usecase.movie.GetMoviesByGenreUseCase
import com.example.movieapp.domain.api.usecase.movie.SearchMoviesUseCase
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieGenreViewModel @Inject constructor(
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _movieList = MutableStateFlow<PagingData<Movie>>(PagingData.empty())
    val movieList get() = _movieList.asStateFlow()

    private var currentGenreId: Int? = null

    fun getMoviesByGenre(genreId: Int?, forceRequest: Boolean) = viewModelScope.launch {
        if (genreId != currentGenreId || forceRequest) {
            currentGenreId = genreId
            getMoviesByGenreUseCase(
                BuildConfig.API_KEY,
                Constants.Movie.LANGUAGE,
                genreId = genreId
            ).cachedIn(viewModelScope).collectLatest { pagindData ->
                _movieList.emit(pagindData)
            }
        }
    }

    fun searchMovies(query: String?): Flow<PagingData<Movie>> {
        return searchMoviesUseCase(
            BuildConfig.API_KEY,
            Constants.Movie.LANGUAGE,
            query = query
        ).cachedIn(viewModelScope)
    }

}