package com.example.ta_mobile.ui.buyer.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import kotlinx.coroutines.launch

class BuyerCartViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {
    fun getProductCart() = buyerRepository.getCartProduct().asLiveData().distinctUntilChanged()

    fun removeProduct(productId:String){
        viewModelScope.launch {
            buyerRepository.deleteByProductId(productId)
        }
    }

    fun updateProductAmount(productId: String, amount:Int){
        viewModelScope.launch {
            buyerRepository.updateCartProductAmount(productId, amount)
        }
    }

}