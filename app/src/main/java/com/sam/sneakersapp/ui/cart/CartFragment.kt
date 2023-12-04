package com.sam.sneakersapp.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sam.sneakersapp.R
import com.sam.sneakersapp.data.model.Sneaker
import com.sam.sneakersapp.databinding.FragmentCartBinding
import com.sam.sneakersapp.ui.MainActivity
import com.sam.sneakersapp.ui.SneakersViewModel
import com.sam.sneakersapp.ui.cart.adapter.CartItemAdapter
import com.sam.sneakersapp.util.ShowDialogParams
import com.sam.sneakersapp.util.showDialog

class CartFragment : Fragment() {

    private var viewBinding: FragmentCartBinding? = null

    private val sneakerViewModel by lazy {
        ViewModelProvider(requireActivity())[SneakersViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCartBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).selectCartCard(true)
        (requireActivity() as MainActivity).selectHomeCard(false)
        (requireActivity() as MainActivity).hideCartIcon(true)
        initUi()
        initCartList()
        initListener()
    }

    private fun initListener() {
        viewBinding?.checkoutBtn?.setOnClickListener {
            sneakerViewModel.clearCart()
           showDialog(
                ShowDialogParams (
                    title = getString(R.string.success),
                    message = getString(R.string.order_placed_successfully),
                    textPositive = getString(R.string.explore_more),
                    positiveListener = {
                        findNavController().navigate(CartFragmentDirections.actionCartFragmentToHomeFragment())
                    },
                    cancelable = false,
                    canceledOnTouchOutside = false
                )
           )
        }
    }

    private fun initUi() {
        (requireActivity() as MainActivity).showBackArrow(true)

        viewBinding?.subtotal?.text =
            getString(R.string.subtotal, sneakerViewModel.getTotalPrice().toString())
        viewBinding?.chargesAndTaxes?.text =
            getString(R.string.taxes_and_charges, sneakerViewModel.getTaxes().toString())
        viewBinding?.totalPrice?.text = getString(
            R.string.total_s,
            (sneakerViewModel.getTotalPrice() + sneakerViewModel.getTaxes()).toString()
        )
    }

    private fun initCartList() {
        sneakerViewModel.sneakersList.observe(viewLifecycleOwner) {
            val cartList = it.sneakers?.filter { sneaker -> sneaker.isAddedToCart }
            if (cartList.isNullOrEmpty()) {
                viewBinding?.cartEmptyView?.visibility = View.VISIBLE
                viewBinding?.cartView?.visibility = View.GONE
            } else {
                viewBinding?.cartEmptyView?.visibility = View.GONE
                viewBinding?.cartView?.visibility = View.VISIBLE
            }
            val cartItemAdapter = CartItemAdapter { sneaker, id, _ ->
                setAdapterListener(id, sneaker)
            }.apply {
                it.sneakers?.filter { sneaker -> sneaker.isAddedToCart }
                    ?.let { it1 -> updateList(it1) }
            }
            viewBinding?.cartRecyclerView?.adapter = cartItemAdapter
        }
    }

    private fun setAdapterListener(id: Int, sneaker: Sneaker) {
        when (id) {
            R.id.remove_item -> {
                sneakerViewModel.updateCart(sneaker, false)
                viewBinding?.subtotal?.text =
                    getString(
                        R.string.subtotal,
                        sneakerViewModel.getTotalPrice().toString()
                    )
                viewBinding?.chargesAndTaxes?.text =
                    getString(
                        R.string.taxes_and_charges,
                        sneakerViewModel.getTaxes().toString()
                    )
                viewBinding?.totalPrice?.text = getString(
                    R.string.total_s,
                    (sneakerViewModel.getTotalPrice() + sneakerViewModel.getTaxes()).toString()
                )
            }

            else -> {
                findNavController().navigate(
                    CartFragmentDirections.actionCartFragmentToSneakerDetailsFragment(
                        sneaker
                    )
                )
            }
        }
    }
}