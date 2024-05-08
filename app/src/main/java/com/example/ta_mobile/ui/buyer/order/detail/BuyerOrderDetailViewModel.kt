package com.example.ta_mobile.ui.buyer.order.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.source.remote.response.order.AddSellerRatingResponse
import com.example.ta_mobile.data.source.remote.response.order.OrderDetailResponse
import com.example.ta_mobile.data.source.remote.response.order.UpdateOrderStatusResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class BuyerOrderDetailViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {

    private var _orderDetailData = MutableLiveData<NetworkResult<OrderDetailResponse>>()
    val orderDetailData : LiveData<NetworkResult<OrderDetailResponse>> = _orderDetailData

    private var _updateOrderStatusResult = MutableLiveData<NetworkResult<UpdateOrderStatusResponse>>()
    val updateOrderStatusResult : LiveData<NetworkResult<UpdateOrderStatusResponse>> = _updateOrderStatusResult

    private var _addOrderRatingResult = MutableLiveData<NetworkResult<AddSellerRatingResponse>>()
    val addOrderRatingResult : LiveData<NetworkResult<AddSellerRatingResponse>> = _addOrderRatingResult

    fun getOrderDetail(transactionId:String){
        viewModelScope.launch {
            buyerRepository.getOrderDetailByTransaction(transactionId).collect{
                _orderDetailData.postValue(it)
            }
        }
    }

    fun updateOrderStatus(transactionId: String){
        viewModelScope.launch {
            buyerRepository.updateOrderStatusDetailByTransaction(transactionId).collect{
                _updateOrderStatusResult.postValue(it)
            }
        }
    }

    fun giveRating(transactionId: String, rating: Float){
        viewModelScope.launch {
            buyerRepository.addSellerRating(transactionId, rating).collect{
                _addOrderRatingResult.postValue(it)
            }
        }
    }
}