package com.sam.sneakersapp.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.sam.sneakersapp.R
import com.sam.sneakersapp.data.model.Size
import com.sam.sneakersapp.databinding.FragmentSneakerDetailsBinding
import com.sam.sneakersapp.ui.MainActivity
import com.sam.sneakersapp.ui.SneakersViewModel
import com.sam.sneakersapp.ui.details.adapter.ColorAdapter
import com.sam.sneakersapp.ui.details.adapter.SizesAdapter

class SneakerDetailsFragment : Fragment() {

    private var viewBinding: FragmentSneakerDetailsBinding? = null

    private val navArgs by navArgs<SneakerDetailsFragmentArgs>()

    private var selectedSize = ""
    private var sizeList = arrayListOf<Size>()
    private var sizeAdapter: SizesAdapter? = null
    private var selectedColor = ""
    private var colorList = arrayListOf<Size>()
    private var colorAdapter: ColorAdapter? = null

    private val sneakerViewModel by lazy {
       ViewModelProvider(requireActivity())[SneakersViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentSneakerDetailsBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initListener()
    }

    private fun initUi() {
        (requireActivity() as MainActivity).showBackArrow(true)
        (requireActivity() as MainActivity).selectCartCard(false)
        (requireActivity() as MainActivity).selectHomeCard(false)
        (requireActivity() as MainActivity).hideCartIcon(false)
        Glide.with(requireContext())
            .load(navArgs.sneaker.media?.imageUrl)
            .into(viewBinding?.sneakerImage!!)

        viewBinding?.sneakerName?.text = navArgs.sneaker.name
        viewBinding?.sneakerPrice?.text =
            getString(R.string.retail_price, navArgs.sneaker.retailPrice.toString())
        viewBinding?.sneakerBrand?.text = navArgs.sneaker.brand
        viewBinding?.releaseDate?.text = navArgs.sneaker.releaseYear


        if (navArgs.sneaker.isAddedToCart) {
            viewBinding?.addToCartBtn?.text = getString(R.string.go_to_cart)
        } else {
            viewBinding?.addToCartBtn?.text = getString(R.string.add_to_cart)
        }

        navArgs.sneaker.size?.let {
            it.forEach { size ->
                sizeList.add(Size(size, false))
            }
            sizeAdapter = SizesAdapter(onSizeItemClickWithId())
            sizeAdapter?.updateList(sizeList)
            viewBinding?.sneakerSizeRecyclerView?.adapter = sizeAdapter
        }

        navArgs.sneaker.color?.let {
            it.forEach { color ->
                colorList.add(Size(color, false))
            }
            colorAdapter = ColorAdapter(onColorItemClickWithId())
            colorAdapter?.updateList(colorList)
            viewBinding?.sneakerColorRecyclerView?.adapter = colorAdapter
        }
    }

    private fun initListener() {
        viewBinding?.addToCartBtn?.setOnClickListener {
            if (navArgs.sneaker.isAddedToCart) {
                findNavController().navigate(R.id.action_sneakerDetailsFragment_to_cartFragment)
            } else {
                viewBinding?.addToCartBtn?.text = getString(R.string.go_to_cart)
                sneakerViewModel.updateCart(navArgs.sneaker, true)
                navArgs.sneaker.isAddedToCart = true
            }
        }
    }

    private fun onSizeItemClickWithId(): (Size, Int) -> Unit = { size, pos ->
        if (size.isSelected) {
            size.isSelected = false
            sizeAdapter?.updateItemSelection(pos, false)
        } else {
            selectedSize = size.size.toString()
            size.isSelected = true
            sizeAdapter?.updateItemSelection(pos, true)
        }
    }

    private fun onColorItemClickWithId(): (Size, Int) -> Unit = { size, pos ->
        if (size.isSelected) {
            size.isSelected = false
            colorAdapter?.updateItemSelection(pos, false)
        } else {
            selectedColor = size.size.toString()
            size.isSelected = true
            colorAdapter?.updateItemSelection(pos, true)
        }
    }
}