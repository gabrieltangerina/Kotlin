package com.example.movieapp.presenter.main.moviegenre.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.MovieGenreItemBinding
import com.example.movieapp.domain.model.Movie

class MoviePagingAdapter(
    private val context: Context,
    private val movieClickListener: (Int?) -> Unit
) : PagingDataAdapter<Movie, MoviePagingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
             MovieGenreItemBinding.inflate(
                 LayoutInflater.from(parent.context),
                 parent,
                 false
             )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = getItem(position)

        holder.itemView.setOnClickListener { movieClickListener(movie?.id) }

        Glide
            .with(context)
            .load("https://image.tmdb.org/t/p/w500${movie?.posterPath}")
            .into(holder.binding.movieImage)

    }

    inner class MyViewHolder(val binding: MovieGenreItemBinding)
        : RecyclerView.ViewHolder(binding.root)

}