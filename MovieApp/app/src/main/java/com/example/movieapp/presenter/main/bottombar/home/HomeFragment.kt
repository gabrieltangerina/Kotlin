package com.example.movieapp.presenter.main.bottombar.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.presenter.main.bottombar.home.adapter.GenreMovieAdapter
import com.example.movieapp.presenter.model.MoviesByGenre
import com.example.movieapp.util.StateView
import com.example.movieapp.util.animatedNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var genreMovieAdapter: GenreMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
        initObservers()
    }

    private fun initObservers() {
        viewModel.homeState.observe(viewLifecycleOwner){stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.recyclerGenres.isVisible = false
                }
                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    binding.recyclerGenres.isVisible = true
                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    binding.recyclerGenres.isVisible = false
                }
            }
        }

        viewModel.movieList.observe(viewLifecycleOwner){moviesByGenre ->
            genreMovieAdapter.submitList(moviesByGenre)
        }
    }

    private fun initRecycler() {
        genreMovieAdapter = GenreMovieAdapter(
            showAllListener = { genreId, genreName ->
                val action = HomeFragmentDirections
                    .actionMenuHomeToMovieGenreFragment(genreId, genreName)

                findNavController().animatedNavigate(action)
            },
            movieClickListener = { movieId ->
                movieId?.let {
                    val action = MainGraphDirections.actionGlobalMovieDetailsFragment(it)
                    findNavController().animatedNavigate(action)
                }
            }
        )

        with(binding.recyclerGenres) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = genreMovieAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}