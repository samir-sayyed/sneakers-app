package com.sam.sneakersapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sam.sneakersapp.data.SneakerRepository
import com.sam.sneakersapp.data.model.Sneaker
import com.sam.sneakersapp.data.model.SneakerList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class SneakersViewModel @Inject constructor(
    private val sneakerRepository: SneakerRepository
) : ViewModel() {

    private val _sneakersList = MutableLiveData<SneakerList>()
    val sneakersList: LiveData<SneakerList> = _sneakersList

    val cartSize = MutableLiveData(0)

    fun getSneakers() = viewModelScope.launch {
        sneakerRepository.getSneakers().collectLatest {
            _sneakersList.value = it
        }
    }

   fun updateCart(sneaker: Sneaker, isAddedToCart: Boolean) {
        val list = sneakersList.value?.sneakers
        list?.forEach {
            if (it.id == sneaker.id) {
                it.isAddedToCart = isAddedToCart
            }
        }
        _sneakersList.postValue(SneakerList(list))
       cartSize.postValue(_sneakersList.value?.sneakers?.filter { sneaker -> sneaker.isAddedToCart }?.size)
    }

    fun getTotalPrice(): Int {
        var totalPrice = 0.0
        val list = sneakersList.value?.sneakers
        list?.forEach {
            if (it.isAddedToCart && it.retailPrice != null) {
                totalPrice += it.retailPrice
            }
        }
        return totalPrice.roundToInt()
    }

    fun getTaxes(): Int {
        return (getTotalPrice() * 0.13).roundToInt()
    }

    fun clearCart() {
        val list = sneakersList.value?.sneakers
        list?.forEach {
            it.isAddedToCart = false
        }
        _sneakersList.postValue(SneakerList(list))
        cartSize.postValue(0)
    }

}