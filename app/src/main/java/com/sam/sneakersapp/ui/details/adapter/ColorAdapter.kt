package com.sam.sneakersapp.ui.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sam.sneakersapp.data.model.Size
import com.sam.sneakersapp.databinding.ColorItemBinding

class ColorAdapter(private val onClick: (Size, Int) -> Unit) :
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    private val colorList = mutableListOf<Size>()

    fun updateList(list: List<Size>) {
        colorList.clear()
        colorList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ColorViewHolder(val viewBinding: ColorItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val viewBinding =
            ColorItemBinding.inflate(LayoutInflater.from(parent.context))
        return ColorViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val color = colorList[position]
        holder.viewBinding.colorImage.setImageResource(
            when (color.size) {
                "red" -> {
                    android.R.color.holo_red_dark
                }
                "blue" -> {
                    android.R.color.holo_blue_dark
                }
                "green" -> {
                    android.R.color.holo_green_dark
                }
                "yellow" -> {
                    android.R.color.holo_orange_dark
                }
                else -> {
                    android.R.color.black
                }
            }.toInt()
        )
        when (color.isSelected) {
              true -> {
                 holder.viewBinding.checked.visibility = View.VISIBLE
                }
                false -> {
                    holder.viewBinding.checked.visibility = View.GONE
                }
            }
        holder.viewBinding.root.setOnClickListener {
            onClick(color, position)
        }
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    fun updateItemSelection(position: Int, isSelected: Boolean) {
        colorList.forEach {
            it.isSelected = false
        }
        val current: Size = colorList[position]
        current.isSelected = isSelected
        notifyDataSetChanged()
    }
}
