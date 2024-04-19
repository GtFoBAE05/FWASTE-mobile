package com.example.ta_mobile.ui.buyer.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class BuyerSearchStoreViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {
    private var _searchStoreResult = MutableLiveData<NetworkResult<SearchStoreResponse>>()
    val searchStoreResult: LiveData<NetworkResult<SearchStoreResponse>> = _searchStoreResult

    fun getStoreNearBuyer(keyword:String){
        viewModelScope.launch {
            buyerRepository.searchStore(keyword).collect{
                _searchStoreResult.postValue(it)
            }
        }
    }

}