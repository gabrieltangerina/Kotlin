package com.example.movieapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.data.api.ServiceAPI
import com.example.movieapp.data.model.movie.MovieResponse
import com.example.movieapp.util.Constants.Paging.DEFAULT_PAGE_INDEX

class MovieByGenrePagingSource(
    private val serviceAPI: ServiceAPI,
    private val genreId: Int?
) : PagingSource<Int, MovieResponse>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, MovieResponse> {
        return try {
            val page = params.key ?: DEFAULT_PAGE_INDEX
            val result = serviceAPI.getMoviesByGenrePagination(
                genreId = genreId,
                page = page
            ).results ?: emptyList()
            return LoadResult.Page(
                data = result,
                prevKey = if(page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if(result.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}