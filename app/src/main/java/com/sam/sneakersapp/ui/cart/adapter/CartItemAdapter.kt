package com.sam.sneakersapp.ui.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sam.sneakersapp.data.model.Sneaker
import com.sam.sneakersapp.databinding.CartItemBinding

class CartItemAdapter(private val onClick: (Sneaker, Int, Int) -> Unit) :
    RecyclerView.Adapter<CartItemAdapter.SneakerViewHolder>() {

    private val sneakerList = mutableListOf<Sneaker>()

    fun updateList(list: List<Sneaker>) {
        sneakerList.clear()
        sneakerList.addAll(list)
        notifyDataSetChanged()
    }

    inner class SneakerViewHolder(val viewBinding: CartItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SneakerViewHolder {
        val viewBinding =
            CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SneakerViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: SneakerViewHolder, position: Int) {
        val sneaker = sneakerList[position]
        holder.viewBinding.sneakerName.text = sneaker.name
        holder.viewBinding.sneakerPrice.text = "$${sneaker.retailPrice}"

        Glide.with(holder.viewBinding.sneakerImage.context)
            .load(sneaker.media?.imageUrl)
            .into(holder.viewBinding.sneakerImage)
        holder.viewBinding.removeItem.setOnClickListener {
            onClick(sneaker, it.id, position)
        }
        holder.viewBinding.root.setOnClickListener {
            onClick(sneaker, it.id, position)
        }
    }

    override fun getItemCount(): Int {
        return sneakerList.size
    }

}
