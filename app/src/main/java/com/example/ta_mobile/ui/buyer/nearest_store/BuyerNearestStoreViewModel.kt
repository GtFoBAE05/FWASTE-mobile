package com.example.ta_mobile.ui.buyer.nearest_store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.source.remote.response.buyer.nearest_store.NearestStoreResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class BuyerNearestStoreViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {

    private var _userNearestStore = MutableLiveData<NetworkResult<NearestStoreResponse>>()
    val userNearestStore : LiveData<NetworkResult<NearestStoreResponse>> = _userNearestStore


     fun getNearestStore(lat: Double, long:Double, radius:Double){
        viewModelScope.launch {
            buyerRepository.getNearestStore(lat, long, radius).collect{
                _userNearestStore.postValue(it)
            }
        }
    }

}