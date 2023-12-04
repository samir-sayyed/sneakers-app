package com.sam.sneakersapp.ui.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sam.sneakersapp.R
import com.sam.sneakersapp.data.model.Size
import com.sam.sneakersapp.databinding.SizeItemBinding

class SizesAdapter(private val onClick: (Size, Int) -> Unit) :
    RecyclerView.Adapter<SizesAdapter.SizeViewHolder>() {

    private val sizeList = mutableListOf<Size>()

    fun updateList(list: List<Size>) {
        sizeList.clear()
        sizeList.addAll(list)
        notifyDataSetChanged()
    }

    inner class SizeViewHolder(val viewBinding: SizeItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val viewBinding =
            SizeItemBinding.inflate(LayoutInflater.from(parent.context))
        return SizeViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val size = sizeList[position]
        holder.viewBinding.tvSize.text = size.size
        when (size.isSelected) {
              true -> {
                  holder.viewBinding.tvSize.setBackgroundResource(R.color.orange)
                  holder.viewBinding.tvSize.setTextColor(holder.viewBinding.root.context.resources.getColor(R.color.white, null))
                }
                false -> {
                    holder.viewBinding.tvSize.setBackgroundResource(R.drawable.orange_border_bg)
                    holder.viewBinding.tvSize.setTextColor(holder.viewBinding.root.context.resources.getColor(R.color.black, null))
                }
            }
        holder.viewBinding.root.setOnClickListener {
            onClick(size, position)
        }
    }

    override fun getItemCount(): Int {
        return sizeList.size
    }

    fun updateItemSelection(position: Int, isSelected: Boolean) {
        sizeList.forEach {
            it.isSelected = false
        }
        val current: Size = sizeList[position]
        current.isSelected = isSelected
        notifyDataSetChanged()
    }
}
