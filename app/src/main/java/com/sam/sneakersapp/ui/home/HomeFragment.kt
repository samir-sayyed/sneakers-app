package com.sam.sneakersapp.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.sam.sneakersapp.R
import com.sam.sneakersapp.data.model.Sneaker
import com.sam.sneakersapp.databinding.FragmentHomeBinding
import com.sam.sneakersapp.ui.MainActivity
import com.sam.sneakersapp.ui.SneakersViewModel
import com.sam.sneakersapp.ui.home.adapter.SneakerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var viewBinding: FragmentHomeBinding? = null
    private var sneakerAdapter: SneakerAdapter? = null
    private var sneakersList = arrayListOf<Sneaker>()

    private val sneakersViewModel by lazy {
        ViewModelProvider(requireActivity())[SneakersViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sneakersViewModel.sneakersList.value == null)
            sneakersViewModel.getSneakers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).showBackArrow(false)
        (requireActivity() as MainActivity).selectCartCard(false)
        (requireActivity() as MainActivity).selectHomeCard(true)
        (requireActivity() as MainActivity).hideCartIcon(false)
        setSpinnerAdapter()
        setSpinnerListener()
        observeData()
        editTextChangeListener()
    }

    private fun observeData() {
        sneakersViewModel.sneakersList.observe(viewLifecycleOwner) {
            sneakersList = it.sneakers as ArrayList<Sneaker>
            sneakerAdapter = SneakerAdapter(onItemClickWithId())
            sneakerAdapter?.updateList(it.sneakers)
            val layoutManager = GridLayoutManager(requireContext(), 2)
            viewBinding?.recyclerView?.layoutManager = layoutManager
            viewBinding?.recyclerView?.adapter = sneakerAdapter
        }
    }

    private fun editTextChangeListener() {
        viewBinding?.searchBar?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    viewBinding?.noResultsFoundTv?.visibility = View.GONE
                    viewBinding?.recyclerView?.visibility = View.VISIBLE
                    sneakerAdapter?.updateList(sneakersList)
                } else
                    search(s.toString())
            }
        })
    }

    private fun search(keyword: String) {
        val filteredList = arrayListOf<Sneaker>()
        sneakersList.forEach {
            if (it.name?.contains(keyword, true) == true) {
                filteredList.add(it)
            }
        }
        if (filteredList.isEmpty()) {
            viewBinding?.noResultsFoundTv?.visibility = View.VISIBLE
            viewBinding?.recyclerView?.visibility = View.GONE
        }else{
            viewBinding?.noResultsFoundTv?.visibility = View.GONE
            viewBinding?.recyclerView?.visibility = View.VISIBLE
            sneakerAdapter?.updateList(filteredList)
        }
    }

    private fun setSpinnerListener() {
        val notesListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                parent?.getChildAt(position)?.findViewById<TextView>(R.id.spinner)
                    ?.setTextColor(resources.getColor(R.color.orange, null))
                when (position) {
                    1 -> sortSneakersByPrice(true)

                    2 -> sortSneakersByPrice(false)

                    3 -> sortSneakersByReleaseDate(true)

                    4 -> sortSneakersByReleaseDate(false)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        viewBinding?.spinner?.onItemSelectedListener = notesListener
    }

    private fun setSpinnerAdapter() {
        val sortByOptions =
            arrayOf("Sort By", "Price(Low-High)", "Price(High-Low)", "Date(New-Old)", "Date(Old-New)")
        val spinnerAdapter = object : ArrayAdapter<Any?>(
            requireContext(),
            R.layout.spinner_text_item,
            sortByOptions
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                return super.getDropDownView(position, convertView, parent).apply {
                    (this as TextView).setTextColor(
                        resources.getColor(
                            if (position == viewBinding?.spinner?.selectedItemPosition && position != 0) R.color.orange else R.color.black,
                            null
                        )
                    )
                }
            }
        }
        spinnerAdapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        viewBinding?.spinner?.adapter = spinnerAdapter
    }

    private fun onItemClickWithId(): (Sneaker, Int, Int) -> Unit {
        return { sneaker, viewId, position ->
            when (viewId) {
                R.id.sneaker_item_layout -> {
                    findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToSneakerDetailsFragment(
                            sneaker
                        )
                    )
                }
                else -> {
                    when {
                        sneaker.isAddedToCart -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.item_removed_from_cart),
                                Toast.LENGTH_SHORT
                            ).show()
                            sneakersViewModel.cartSize.postValue(
                                sneakersViewModel.cartSize.value?.minus(1)
                            )
                            sneakerAdapter?.updateItemSelection(position, false)
                        }

                        else -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.item_added_to_cart),
                                Toast.LENGTH_SHORT
                            ).show()
                            sneakersViewModel.cartSize.postValue(
                                sneakersViewModel.cartSize.value?.plus(1)
                            )
                            sneakerAdapter?.updateItemSelection(position, true)
                        }
                    }
                }
            }
        }
    }

    fun sortSneakersByPrice(isAscending: Boolean) {
        var sneakersListSorted = listOf<Sneaker>()
        sneakersList.let {
            sneakersListSorted = if (isAscending) {
                sneakersList.sortedBy { sneaker -> sneaker.retailPrice }
            } else {
                it.sortedByDescending { sneaker -> sneaker.retailPrice }
            }
        }
        sneakerAdapter?.updateList(sneakersListSorted)
    }

    fun sortSneakersByReleaseDate(isAscending: Boolean) {
        var sneakersListSorted = listOf<Sneaker>()
        sneakersList.let {
            sneakersListSorted = if (isAscending) {
                sneakersList.sortedBy { sneaker -> sneaker.releaseYear }
            } else {
                it.sortedByDescending { sneaker -> sneaker.releaseYear }
            }
        }
        sneakerAdapter?.updateList(sneakersListSorted)
    }

}