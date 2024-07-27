package com.example.movieapp.presenter.main.moviedetails.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieDetailsBinding
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presenter.main.moviedetails.similar.SimilarMoviesFragment
import com.example.movieapp.presenter.main.moviedetails.trailers.TrailersFragment
import com.example.movieapp.presenter.main.moviedetails.adapter.CastAdapter
import com.example.movieapp.presenter.main.moviedetails.adapter.ViewPagerAdapter
import com.example.movieapp.presenter.main.moviedetails.comments.CommentsFragment
import com.example.movieapp.util.StateView
import com.example.movieapp.util.ViewPager2ViewHeightAnimator
import com.example.movieapp.util.getYearFromDate
import com.example.movieapp.util.initToolbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by activityViewModels()
    private val args: MovieDetailsFragmentArgs by navArgs()

    private lateinit var castAdapter: CastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(toolbar = binding.toolbar, lightIcon = true)
        initRecyclerCredits()
        getMovieDetails()
        configTabLayout()
    }

    private fun configTabLayout(){
        viewModel.setMovieId(movieId = args.movieId)

        val adapter = ViewPagerAdapter(requireActivity())
        val mViewPager = ViewPager2ViewHeightAnimator()

        mViewPager.viewPager2 = binding.viewPager
        mViewPager.viewPager2?.adapter = adapter

        adapter.addFragment(TrailersFragment(), R.string.title_trailers_tab_layout)
        adapter.addFragment(SimilarMoviesFragment(), R.string.title_similar_tab_layout)
        adapter.addFragment(CommentsFragment(), R.string.title_comments_tab_layout)

        binding.viewPager.offscreenPageLimit = adapter.itemCount

        mViewPager.viewPager2?.let {viewPager2 ->
            TabLayoutMediator(binding.tabs, viewPager2){tab, position ->
                tab.text = getString(adapter.getTitle(position))
            }.attach()
        }

    }

    private fun getMovieDetails() {
        viewModel.getMovieDetails(args.movieId).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {
                    configData(stateView.data)
                }

                is StateView.Error -> {

                }
            }
        }
    }

    private fun getCredits() {
        viewModel.getCredits(args.movieId).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {
                    castAdapter.submitList(stateView.data?.cast)
                }

                is StateView.Error -> {

                }
            }
        }
    }

    private fun initRecyclerCredits() {
        castAdapter = CastAdapter()

        with(binding.recyclerCast) {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = castAdapter
        }
    }

    private fun configData(movie: Movie?) {

        Glide
            .with(requireContext())
            .load("https://image.tmdb.org/t/p/w500${movie?.posterPath}")
            .into(binding.imageMovie)

        binding.textMovie.text = movie?.title
        binding.textVoteAverage.text = String.format("%.1f", movie?.voteAverage)
        binding.textProductionCountry.text = movie?.productionCountries?.get(0)?.name ?: ""
        binding.textReleaseDate.text = movie?.releaseDate?.getYearFromDate()

        val genres = movie?.genres?.map { it.name }?.joinToString(", ")
        binding.textGenres.text = getString(R.string.text_all_genres_movie_details_fragment, genres)

        binding.textDescription.text = movie?.overview

        getCredits()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}