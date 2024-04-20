package com.example.ta_mobile.ui.buyer.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.source.remote.response.buyer.order.BuyerOrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.buyer.order.BuyerOrderStatusResponseData
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class BuyerOrderStatusViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {
    private var _orderStatusResult = MutableLiveData<NetworkResult<BuyerOrderStatusResponse>>()
    val orderStatusResult: LiveData<NetworkResult<BuyerOrderStatusResponse>> = _orderStatusResult

    fun getOrderStatus(keyword:String){
        viewModelScope.launch {
            buyerRepository.getOrderByStatus(keyword).collect{
                _orderStatusResult.postValue(it)
            }
        }
    }

}