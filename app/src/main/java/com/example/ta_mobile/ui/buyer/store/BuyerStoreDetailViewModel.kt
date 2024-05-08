package com.example.ta_mobile.ui.buyer.store

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.source.remote.response.buyer.favourite_store.AddFavouriteStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.favourite_store.GetFavouriteStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.favourite_store.RemoveFavouriteStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.StoreDetailResponse
import com.example.ta_mobile.utils.NetworkResult
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

class BuyerStoreDetailViewModel(private val buyerRepository: BuyerRepository) : ViewModel() {
    private var _StoreDetailResult = MutableLiveData<NetworkResult<StoreDetailResponse>>()
    val storeDetailResponse : LiveData<NetworkResult<StoreDetailResponse>> = _StoreDetailResult

    private var _favStoreResult = MutableLiveData<NetworkResult<GetFavouriteStoreResponse>>()
    val favStoreResult: LiveData<NetworkResult<GetFavouriteStoreResponse>> = _favStoreResult

    private var _addFavStoreResult = MutableLiveData<NetworkResult<AddFavouriteStoreResponse>>()
    val addFavStoreResult: LiveData<NetworkResult<AddFavouriteStoreResponse>> = _addFavStoreResult

    private var _removeFavStoreResult = MutableLiveData<NetworkResult<RemoveFavouriteStoreResponse>>()
    val removeFavStoreResult: LiveData<NetworkResult<RemoveFavouriteStoreResponse>> = _removeFavStoreResult

    fun getStoreDetail(storeId : String){
        viewModelScope.launch {
            buyerRepository.getStoreDetail(storeId).collect{
                _StoreDetailResult.postValue(it)
            }
        }
    }

    fun getFavouriteStore() {
        viewModelScope.launch {
            buyerRepository.getFavouriteStore().collect {
                _favStoreResult.postValue(it)
            }
        }
    }

    fun addFavouriteStore(storeId: String) {
        viewModelScope.launch {
            buyerRepository.addFavouriteStore(storeId).collect {
                _addFavStoreResult.postValue(it)
                getFavouriteStore()
            }
        }
    }

    fun removeFavouriteStore(id: String) {
        viewModelScope.launch {
            buyerRepository.removeFavouriteStore(id).collect {
                _removeFavStoreResult.postValue(it)
                getFavouriteStore()
            }
        }
    }

     fun subscribeTopic(topic:String){
        FirebaseMessaging.getInstance()
            .subscribeToTopic(topic)
            .addOnSuccessListener { unused: Void? ->
                Log.i(
                    ContentValues.TAG,
                    "Subscribed to topic: " + topic
                )

            }
    }

     fun unsubscribeTopic(topic: String){
        FirebaseMessaging.getInstance()
            .unsubscribeFromTopic(topic)
            .addOnSuccessListener { unused: Void? ->
                Log.i(
                    ContentValues.TAG,
                    "Unsubscribed from topic: " + topic
                )

            }
    }

}