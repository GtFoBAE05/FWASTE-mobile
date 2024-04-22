package com.example.ta_mobile.ui.seller.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.SellerRepository
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMyProductResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class SellerProductViewModel(private val sellerRepository: SellerRepository) : ViewModel() {


    private var _myProductData = MutableLiveData<NetworkResult<SellerGetMyProductResponse>>()
    val myProductData : LiveData<NetworkResult<SellerGetMyProductResponse>> = _myProductData

    fun getMyProduct(){
        viewModelScope.launch {
            sellerRepository.getMyProduct().collect{
                _myProductData.postValue(it)
            }
        }
    }
}