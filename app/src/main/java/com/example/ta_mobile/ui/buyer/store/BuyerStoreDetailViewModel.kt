package com.example.ta_mobile.ui.buyer.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.source.remote.response.buyer.store.StoreDetailResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class BuyerStoreDetailViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {
    private var _StoreDetailResult = MutableLiveData<NetworkResult<StoreDetailResponse>>()
    val storeDetailResponse : LiveData<NetworkResult<StoreDetailResponse>> = _StoreDetailResult


    fun getStoreDetail(storeId : String){
        viewModelScope.launch {
            buyerRepository.getStoreDetail(storeId).collect{
                _StoreDetailResult.postValue(it)
            }
        }
    }

}