package com.sam.sneakersapp.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sam.sneakersapp.R
import com.sam.sneakersapp.data.model.Sneaker
import com.sam.sneakersapp.databinding.SneakerItemBinding

class SneakerAdapter(private val onClick: (Sneaker, Int, Int) -> Unit) :
    RecyclerView.Adapter<SneakerAdapter.SneakerViewHolder>() {

    private val sneakerList = mutableListOf<Sneaker>()

    fun updateList(list: List<Sneaker>) {
        sneakerList.clear()
        sneakerList.addAll(list)
        notifyDataSetChanged()
    }

    inner class SneakerViewHolder(val viewBinding: SneakerItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SneakerViewHolder {
        val viewBinding =
            SneakerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SneakerViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: SneakerViewHolder, position: Int) {
        val sneaker = sneakerList[position]
        holder.viewBinding.sneakerName.text = sneaker.name
        holder.viewBinding.sneakerPrice.text = "$${sneaker.retailPrice}"

        when (sneaker.isAddedToCart) {
            false -> {
                holder.viewBinding.addToCartImage.setImageResource(R.drawable.add_icon)
            }
            else -> {
                holder.viewBinding.addToCartImage.setImageResource(R.drawable.checked_icon)
            }
        }

        Glide.with(holder.viewBinding.sneakerImage.context)
            .load(sneaker.media?.imageUrl)
            .into(holder.viewBinding.sneakerImage)
        holder.viewBinding.sneakerItemLayout.setOnClickListener {
            onClick(sneaker, it.id, position)
        }

        holder.viewBinding.addToCartImage.setOnClickListener {
            onClick(sneaker, it.id, position)
        }
    }

    override fun getItemCount(): Int {
        return sneakerList.size
    }

    fun updateItemSelection(position: Int, isAddedToCart: Boolean) {
        val current: Sneaker = sneakerList[position]
        current.isAddedToCart = isAddedToCart
        notifyDataSetChanged()
    }
}
