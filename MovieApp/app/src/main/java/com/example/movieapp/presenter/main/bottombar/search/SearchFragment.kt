package com.example.movieapp.presenter.main.bottombar.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import br.com.hellodev.movieapp.presenter.main.moviegenre.adapter.LoadStatePagingAdapter
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.presenter.main.bottombar.home.adapter.MovieAdapter
import com.example.movieapp.presenter.main.moviegenre.adapter.MoviePagingAdapter
import com.example.movieapp.util.StateView
import com.example.movieapp.util.animatedNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var moviePagingAdapter: MoviePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        initSearchView()
    }

    private fun searchMovies(query: String) {
        lifecycleScope.launch {
            viewModel.searchMovies(query).collectLatest { pagingData ->
                moviePagingAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }
    }

    private fun clearListMovies() {
        lifecycleScope.launch {
            moviePagingAdapter.submitData(PagingData.empty())
        }
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() || newText.isNotBlank()) searchMovies(newText)
                return false
            }
        })

        val closeButton: View? = binding.searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeButton?.setOnClickListener {
            binding.searchView.setQuery("", false)
            binding.searchView.clearFocus()
            binding.layoutEmpty.isVisible = false
            clearListMovies()
        }
    }

    private fun initRecycler() {
        moviePagingAdapter = MoviePagingAdapter(
            context = requireContext(),
            movieClickListener = { movieId ->
                movieId?.let {
                    val action = MainGraphDirections.actionGlobalMovieDetailsFragment(it)
                    findNavController().animatedNavigate(action)
                }
            }
        )

        lifecycleScope.launch {
            moviePagingAdapter.loadStateFlow.collectLatest { loadState ->
                when(loadState.refresh){
                    LoadState.Loading -> {
                        binding.recyclerMovies.isVisible = false
                        binding.shimmer.startShimmer()
                        binding.shimmer.isVisible = true
                    }

                    is LoadState.NotLoading -> {
                        binding.shimmer.stopShimmer()
                        binding.shimmer.isVisible = false
                        binding.recyclerMovies.isVisible = true

                        emptyState(moviePagingAdapter.itemCount == 0)
                    }

                    is LoadState.Error -> {
                        binding.recyclerMovies.isVisible = true
                        binding.shimmer.stopShimmer()
                        binding.shimmer.isVisible = false

                        val error = (loadState.refresh as LoadState.Error).error.message
                            ?: "Ocorreu um erro. Tente novamente mais tarde"
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        with(binding.recyclerMovies) {
            setHasFixedSize(true)

            // Código para centralizar a progressBar de Loading para carregar mais filmes
            // Força a exposição de uma view em um gridlayout, ignorando a qtnd de colunas

            val gridLayoutManager = GridLayoutManager(requireContext(), 2)
            layoutManager = gridLayoutManager

            val footerAdapter = moviePagingAdapter.withLoadStateFooter(
                footer = LoadStatePagingAdapter()
            )

            adapter = footerAdapter

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    return if(position == moviePagingAdapter.itemCount && footerAdapter.itemCount > 0){
                        2
                    }else{
                        1
                    }
                }
            }

        }
    }

    private fun emptyState(empty: Boolean){
        binding.recyclerMovies.isVisible = !empty
        binding.layoutEmpty.isVisible = empty
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}