package com.example.movieapp.presenter.main.bottombar.profile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.databinding.ItemUserProfileBinding
import com.example.movieapp.domain.model.MenuProfile
import com.example.movieapp.domain.model.MenuProfileType

class ProfileMenuAdapter(
    private val context: Context,
    private val onClick : (MenuProfileType) -> Unit
) : RecyclerView.Adapter<ProfileMenuAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemUserProfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return MenuProfile.items_menu.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val items = MenuProfile.getItems()
        val item = items[position]

        holder.binding.textItemProfile.apply {
            text = context.getString(item.text)

            if(item.type == MenuProfileType.LOGOUT){
                setTextColor(ContextCompat.getColor(context, R.color.color_default))
            }

        }

        holder.binding.imageItemProfile.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                item.icon
            )
        )

        holder.itemView.setOnClickListener { onClick(item.type)  }
    }

    inner class MyViewHolder(val binding: ItemUserProfileBinding) :
        RecyclerView.ViewHolder(binding.root)

}

