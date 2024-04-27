package com.example.ta_mobile.ui.seller.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.SellerRepository
import com.example.ta_mobile.data.source.remote.response.order.OrderDetailResponse
import com.example.ta_mobile.data.source.remote.response.order.OrderStatusResponse
import com.example.ta_mobile.data.source.remote.response.order.UpdateOrderStatusResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class SellerOrderViewModel(private val sellerRepository: SellerRepository) : ViewModel() {
    private var _orderStatusResult = MutableLiveData<NetworkResult<OrderStatusResponse>>()
    val orderStatusResult: LiveData<NetworkResult<OrderStatusResponse>> = _orderStatusResult

    private var _orderDetailData = MutableLiveData<NetworkResult<OrderDetailResponse>>()
    val orderDetailData : LiveData<NetworkResult<OrderDetailResponse>> = _orderDetailData

    private var _updateOrderStatusResult = MutableLiveData<NetworkResult<UpdateOrderStatusResponse>>()
    val updateOrderStatusResult : LiveData<NetworkResult<UpdateOrderStatusResponse>> = _updateOrderStatusResult

    fun getOrderDetail(transactionId:String){
        viewModelScope.launch {
            sellerRepository.getOrderDetailByTransaction(transactionId).collect{
                _orderDetailData.postValue(it)
            }
        }
    }

    fun updateOrderStatus(transactionId: String){
        viewModelScope.launch {
            sellerRepository.updateOrderStatusDetailByTransaction(transactionId).collect{
                _updateOrderStatusResult.postValue(it)
            }
        }
    }

    fun getOrderStatus(keyword:String){
        viewModelScope.launch {
            sellerRepository.getOrderByStatus(keyword).collect{
                _orderStatusResult.postValue(it)
            }
        }
    }

}