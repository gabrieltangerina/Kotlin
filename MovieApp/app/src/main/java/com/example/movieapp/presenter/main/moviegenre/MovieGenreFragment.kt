package com.example.movieapp.presenter.main.moviegenre

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import br.com.hellodev.movieapp.presenter.main.moviegenre.adapter.LoadStatePagingAdapter
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieGenreBinding
import com.example.movieapp.presenter.main.moviegenre.adapter.MoviePagingAdapter
import com.example.movieapp.util.StateView
import com.example.movieapp.util.initToolbar
import com.ferfalk.simplesearchview.SimpleSearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieGenreFragment : Fragment() {

    private var _binding: FragmentMovieGenreBinding? = null
    private val binding get() = _binding!!

    private val args: MovieGenreFragmentArgs by navArgs()

    private lateinit var moviePagingAdapter: MoviePagingAdapter
    private val viewModel: MovieGenreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieGenreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        binding.toolbar.title = args.genreName
        initRecycler()
        getMoviesByGenre()
        initSearchView()
    }

    private fun initRecycler() {
        moviePagingAdapter = MoviePagingAdapter(
            context = requireContext(),
            movieClickListener = { movieId ->
                movieId?.let {
                    val action = MainGraphDirections.actionGlobalMovieDetailsFragment(it)
                    findNavController().navigate(action)
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

    private fun getMoviesByGenre(forceRequest: Boolean = false) {
        lifecycleScope.launch {
            viewModel.getMoviesByGenre(genreId = args.genreId, forceRequest = forceRequest)
            viewModel.movieList.collectLatest { pagingData ->
                moviePagingAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }
    }

    private fun searchMovies(query: String) {
        viewModel.searchMovies(query).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.recyclerMovies.isVisible = false
                    binding.shimmer.startShimmer()
                    binding.shimmer.isVisible = true
                }

                is StateView.Success -> {
                    binding.shimmer.stopShimmer()
                    binding.shimmer.isVisible = false
                    getMoviesByGenre(forceRequest = true)
                    binding.recyclerMovies.isVisible = true
                }

                is StateView.Error -> {
                    binding.shimmer.stopShimmer()
                    binding.shimmer.isVisible = false
                }
            }
        }
    }

    private fun initSearchView() {
        binding.simpleSearchView.setOnQueryTextListener(object :
            SimpleSearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty() || newText.isNotBlank()) searchMovies(newText)
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                getMoviesByGenre()
                return false
            }
        })

        binding.simpleSearchView.setOnSearchViewListener(object :
            SimpleSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                Log.d("SimpleSearchView", "onSearchViewShown")
            }

            override fun onSearchViewClosed() {
                getMoviesByGenre()
            }

            override fun onSearchViewShownAnimation() {
                Log.d("SimpleSearchView", "onSearchViewShownAnimation")
            }

            override fun onSearchViewClosedAnimation() {
                Log.d("SimpleSearchView", "onSearchViewClosedAnimation")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search_view, menu)
        val item = menu.findItem(R.id.action_search)
        binding.simpleSearchView.setMenuItem(item)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}