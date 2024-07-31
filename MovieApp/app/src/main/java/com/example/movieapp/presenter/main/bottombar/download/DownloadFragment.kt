package com.example.movieapp.presenter.main.bottombar.download

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentDownloadBinding
import com.example.movieapp.databinding.FragmentMovieGenreBinding
import com.example.movieapp.presenter.main.bottombar.download.adapter.DownloadMovieAdapter
import com.example.movieapp.presenter.main.bottombar.home.adapter.MovieAdapter
import com.ferfalk.simplesearchview.SimpleSearchView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadFragment : Fragment() {

    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: DownloadMovieAdapter

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
            deleteClickListener = {movieId ->

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