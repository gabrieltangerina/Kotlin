package com.example.movieapp.presenter.main.bottombar.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.BuildConfig
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.usecase.movie.SearchMoviesUseCase
import com.example.movieapp.util.Constants
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>> get() = _movieList

    private val _searchState = MutableLiveData<StateView<Unit>>()
    val searchState: LiveData<StateView<Unit>> get() = _searchState

    fun searchMovies(query: String?) {
        viewModelScope.launch {
            try {
                _searchState.postValue(StateView.Loading())

                val movies = searchMoviesUseCase.invoke(
                    BuildConfig.API_KEY,
                    Constants.Movie.LANGUAGE,
                    query = query
                )

                _movieList.postValue(movies)
                _searchState.postValue(StateView.Success(Unit))

            } catch (ex: HttpException) {
                ex.printStackTrace()
                _searchState.postValue(StateView.Error(ex.message))
            } catch (ex: Exception) {
                ex.printStackTrace()
                _searchState.postValue(StateView.Error(ex.message))
            }
        }
    }

    fun clearMovies() {
        _movieList.postValue(emptyList())
    }

}