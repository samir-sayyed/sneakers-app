package com.sam.sneakersapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.sam.sneakersapp.R
import com.sam.sneakersapp.databinding.ActivityMainBinding
import com.sam.sneakersapp.ui.cart.CartFragment
import com.sam.sneakersapp.ui.cart.CartFragmentDirections
import com.sam.sneakersapp.ui.details.SneakerDetailsFragment
import com.sam.sneakersapp.ui.details.SneakerDetailsFragmentDirections
import com.sam.sneakersapp.ui.home.HomeFragment
import com.sam.sneakersapp.ui.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var viewBinding: ActivityMainBinding? = null

    private val sneakerViewModel by lazy {
        ViewModelProvider(this)[SneakersViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)
        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        sneakerViewModel.cartSize.observe(this) {
            if (it > 0 && navHostFragment?.childFragmentManager?.fragments?.first() !is CartFragment){
                viewBinding?.cartCount?.visibility = View.VISIBLE
                viewBinding?.cartCount?.text = it.toString()
            } else {
                viewBinding?.cartCount?.visibility = View.GONE
            }
        }

        viewBinding?.homeCard?.setOnClickListener {
            if (navHostFragment != null && navHostFragment.childFragmentManager.fragments.isNotEmpty()  && navHostFragment.childFragmentManager.fragments.first() is SneakerDetailsFragment) {
                navHostFragment.navController.navigate(SneakerDetailsFragmentDirections.actionSneakerDetailsFragmentToHomeFragment())
            }else if (navHostFragment != null && navHostFragment.childFragmentManager.fragments.isNotEmpty()  && navHostFragment.childFragmentManager.fragments.first() is CartFragment) {
                navHostFragment.navController.navigate(CartFragmentDirections.actionCartFragmentToHomeFragment())
            }
        }

        viewBinding?.backArrow?.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        viewBinding?.cartIcon?.setOnClickListener {
            if (navHostFragment != null && navHostFragment.childFragmentManager.fragments.isNotEmpty()  && navHostFragment.childFragmentManager.fragments.first() is SneakerDetailsFragment) {
                navHostFragment.navController.navigate(SneakerDetailsFragmentDirections.actionSneakerDetailsFragmentToCartFragment())
            }else if (navHostFragment != null && navHostFragment.childFragmentManager.fragments.isNotEmpty()  && navHostFragment.childFragmentManager.fragments.first() is HomeFragment) {
                navHostFragment.navController.navigate(HomeFragmentDirections.actionHomeFragmentToCartFragment())
            }
        }
    }

    fun selectHomeCard(isSelected: Boolean) {
        if (isSelected) {
            viewBinding?.homeCard?.setCardBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.orange
                )
            )
            viewBinding?.homeIcon?.setImageResource(R.drawable.home_icon)
        } else {
            viewBinding?.homeCard?.setCardBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            viewBinding?.homeIcon?.setImageResource(R.drawable.home_icon_selected)
        }
    }

    fun selectCartCard(isSelected: Boolean) {
        if (isSelected) {
            viewBinding?.cartCard?.setCardBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.orange
                )
            )
            viewBinding?.cartIcon?.setImageResource(R.drawable.cart_icon_selected)
        } else {
            viewBinding?.cartCard?.setCardBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            viewBinding?.cartIcon?.setImageResource(R.drawable.cart_icon)
        }
    }

    fun hideCartIcon(hide: Boolean) {
        if (hide)
            viewBinding?.cartCount?.visibility = View.GONE
        else if (sneakerViewModel.cartSize.value!! > 0)
            viewBinding?.cartCount?.visibility = View.VISIBLE
    }


    fun showBackArrow(show: Boolean) {
        if (show)
            viewBinding?.backArrow?.visibility = View.VISIBLE
        else
            viewBinding?.backArrow?.visibility = View.GONE
    }
}