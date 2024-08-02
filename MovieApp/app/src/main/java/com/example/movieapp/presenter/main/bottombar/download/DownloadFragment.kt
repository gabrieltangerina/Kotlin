package com.example.movieapp.presenter.main.bottombar.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.R
import com.example.movieapp.databinding.BottomSheetDeleteMovieBinding
import com.example.movieapp.databinding.FragmentDownloadBinding
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presenter.main.bottombar.download.adapter.DownloadMovieAdapter
import com.example.movieapp.util.calculateFileSize
import com.example.movieapp.util.calculateMovieTime
import com.ferfalk.simplesearchview.SimpleSearchView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadFragment : Fragment() {

    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: DownloadMovieAdapter
    private val viewModel: DownloadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSearchView()
        initRecycler()
        initObservers()
        getData()
    }

    private fun getData() {
        viewModel.getMovies()
    }

    private fun initObservers() {
        // Get movies downloaded
        viewModel.movieList.observe(viewLifecycleOwner) { movies ->
            mAdapter.submitList(movies)
            emptyState(movies.isEmpty())
        }
    }

    private fun initRecycler() {
        mAdapter = DownloadMovieAdapter(
            context = requireContext(),
            detailsClickListener = { movieId ->
                movieId?.let {
                    val action = MainGraphDirections.actionGlobalMovieDetailsFragment(it)
                    findNavController().navigate(action)
                }
            },
            deleteClickListener = { movie ->
                showBottomSheetDeleteMovie(movie)
            }
        )

        with(binding.rvMovies) {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    private fun initSearchView() {
        binding.simpleSearchView.setOnQueryTextListener(object :
            SimpleSearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // if (newText.isNotEmpty()) searchMovies(newText)
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                // getMoviesByGenre()
                return false
            }
        })

        binding.simpleSearchView.setOnSearchViewListener(object :
            SimpleSearchView.SearchViewListener {
            override fun onSearchViewShown() {

            }

            override fun onSearchViewClosed() {
                //  getMoviesByGenre()
            }

            override fun onSearchViewShownAnimation() {

            }

            override fun onSearchViewClosedAnimation() {

            }
        })
    }

    private fun showBottomSheetDeleteMovie(movie: Movie?) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)

        val bottomSheetBinding = BottomSheetDeleteMovieBinding.inflate(
            layoutInflater, null, false
        )

        Glide
            .with(requireContext())
            .load("https://image.tmdb.org/t/p/w200${movie?.posterPath}")
            .into(bottomSheetBinding.ivMovie)

        bottomSheetBinding.textMovie.text = movie?.title
        bottomSheetBinding.textDuration.text = movie?.runtime?.calculateMovieTime()
        bottomSheetBinding.textSize.text = movie?.runtime?.toDouble()?.calculateFileSize()

        bottomSheetBinding.btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }
        bottomSheetBinding.btnConfirm.setOnClickListener {
            viewModel.deleteMovie(movie?.id)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()

    }

    private fun emptyState(empty: Boolean){
        binding.rvMovies.isVisible = !empty
        binding.layoutEmpty.isVisible = empty
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