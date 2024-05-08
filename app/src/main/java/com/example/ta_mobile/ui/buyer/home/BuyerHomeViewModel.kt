package com.example.ta_mobile.ui.buyer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.repository.UserPrefRepository
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class BuyerHomeViewModel(private val userPrefRepository: UserPrefRepository, private val buyerRepository: BuyerRepository) : ViewModel() {

    private var _storeNearBuyerResult = MutableLiveData<NetworkResult<SearchStoreResponse>>()
    val storeNearBuyerResult: LiveData<NetworkResult<SearchStoreResponse>> = _storeNearBuyerResult

    fun getUserName() = userPrefRepository.getUserName().asLiveData()

    fun getStoreNearBuyer(){
        viewModelScope.launch {
            buyerRepository.searchStore("").collect{
                _storeNearBuyerResult.postValue(it)
            }
        }
    }

}