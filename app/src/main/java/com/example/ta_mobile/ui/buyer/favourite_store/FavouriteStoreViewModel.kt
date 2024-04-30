package com.example.ta_mobile.ui.buyer.favourite_store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.source.remote.response.buyer.favourite_store.GetFavouriteStoreResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class FavouriteStoreViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {

    private var _favStoreResult = MutableLiveData<NetworkResult<GetFavouriteStoreResponse>>()
    val favStoreResult: LiveData<NetworkResult<GetFavouriteStoreResponse>> = _favStoreResult

    fun getFavouriteStore() {
        viewModelScope.launch {
            buyerRepository.getFavouriteStore().collect {
                _favStoreResult.postValue(it)
            }
        }

    }

}