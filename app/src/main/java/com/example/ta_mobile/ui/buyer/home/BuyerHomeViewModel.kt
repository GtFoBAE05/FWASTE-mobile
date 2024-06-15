package com.example.ta_mobile.ui.buyer.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.AuthRepository
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.repository.UserPrefRepository
import com.example.ta_mobile.data.source.remote.response.auth.UserDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.nearest_store.NearestStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerPointResponse
import com.example.ta_mobile.data.source.remote.response.buyer.store.SearchStoreResponse
import com.example.ta_mobile.data.source.remote.response.buyer.voucher.UserOwnedVoucherResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class BuyerHomeViewModel(private val userPrefRepository: UserPrefRepository, private val buyerRepository: BuyerRepository, private val authRepository: AuthRepository) : ViewModel() {

    private var _storeNearBuyerResult = MutableLiveData<NetworkResult<NearestStoreResponse>>()
    val storeNearBuyerResult: LiveData<NetworkResult<NearestStoreResponse>> = _storeNearBuyerResult


    private var _userDetailData = MutableLiveData<NetworkResult<UserDetailResponse>>()
    val userDetailData : LiveData<NetworkResult<UserDetailResponse>> = _userDetailData

    fun getUserName() = userPrefRepository.getUserName().asLiveData()

    fun getNearestStore(lat: Double, long:Double, radius:Double){
        viewModelScope.launch {
            buyerRepository.getNearestStore(lat, long, radius).collect{
                _storeNearBuyerResult.postValue(it)
            }
        }
    }

    fun getUserDetail(){
        viewModelScope.launch {
            authRepository.getUserDetail().collect{
                _userDetailData.postValue(it)
            }
        }
    }

}