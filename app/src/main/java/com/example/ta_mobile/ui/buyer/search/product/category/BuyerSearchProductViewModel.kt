package com.example.ta_mobile.ui.buyer.search.product.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.source.remote.response.buyer.product.SearchProductResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class BuyerSearchProductViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {
    private var _searchProductResult = MutableLiveData<NetworkResult<SearchProductResponse>>()
    val searchProductResult: LiveData<NetworkResult<SearchProductResponse>> = _searchProductResult

    fun searchProductResult(keyword:String){
        viewModelScope.launch {
            buyerRepository.searchProductByCategory(keyword).collect{
                _searchProductResult.postValue(it)
            }
        }
    }

}