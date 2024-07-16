package com.example.burgershub.presenter.burgers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.burgershub.databinding.BurgerItemBinding
import com.example.burgershub.domain.model.Burger
import com.example.burgershub.util.formattedValue
import com.squareup.picasso.Picasso

class BurgersAdapter(
    private val burgers: List<Burger>,
    private val burgerClick: (Int) -> Unit
) : RecyclerView.Adapter<BurgersAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            BurgerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = burgers.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val burger = burgers[position]

        holder.binding.textName.text = burger.name
        holder.binding.textDescription.text = burger.desc
        holder.binding.textPrice.text = burger.price.formattedValue()

        Picasso
            .get()
            .load(burger.image?.get(1)?.lg)
            .into(holder.binding.imageBurger)

        holder.itemView.setOnClickListener { burgerClick(burger.id ?: 0) }

    }

    inner class MyViewHolder(
        val binding: BurgerItemBinding
    ): RecyclerView.ViewHolder(binding.root)

}