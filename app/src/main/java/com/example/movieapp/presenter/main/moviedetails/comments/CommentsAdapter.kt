package com.example.movieapp.presenter.main.moviedetails.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.CastItemBinding
import com.example.movieapp.databinding.ItemCommentBinding
import com.example.movieapp.domain.model.MovieReview
import com.example.movieapp.util.formatCommentDate

class CommentsAdapter : ListAdapter<MovieReview, CommentsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieReview>() {

            override fun areItemsTheSame(oldItem: MovieReview, newItem: MovieReview): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieReview, newItem: MovieReview): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = getItem(position)

        Glide
            .with(holder.binding.root.context)
            .load(comment.authorDetails?.avatarPath)
            .into(holder.binding.imageUser)

        holder.binding.textUsername.text = comment.authorDetails?.username
        holder.binding.textComment.text = comment.content
        holder.binding.textRating.text = comment?.authorDetails?.rating?.toString() ?: "0"
        holder.binding.textDate.text = formatCommentDate(comment.createdAt)

    }

    inner class MyViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root)

}