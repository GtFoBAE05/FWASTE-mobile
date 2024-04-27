package com.example.ta_mobile.ui.seller.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.AuthRepository
import com.example.ta_mobile.data.repository.SellerRepository
import com.example.ta_mobile.data.repository.UserPrefRepository
import com.example.ta_mobile.data.source.remote.response.auth.UpdatePasswordResponse
import com.example.ta_mobile.data.source.remote.response.auth.UserDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerUpdateProfileResponse
import com.example.ta_mobile.data.source.remote.response.seller.profile.SellerUpdateProfileResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class SellerProfileViewModel(
    private val userPrefRepository: UserPrefRepository,
    private val authRepository: AuthRepository,
    private val sellerRepository: SellerRepository

) : ViewModel() {

    private var _userDetailData = MutableLiveData<NetworkResult<UserDetailResponse>>()
    val userDetailData : LiveData<NetworkResult<UserDetailResponse>> = _userDetailData

    private var _updateProfileResult = MutableLiveData<NetworkResult<SellerUpdateProfileResponse>>()
    val updateProfileResult: LiveData<NetworkResult<SellerUpdateProfileResponse>> = _updateProfileResult

    private var _updateUserPasswordesult = MutableLiveData<NetworkResult<UpdatePasswordResponse>>()
    val updateUserPasswordesult: LiveData<NetworkResult<UpdatePasswordResponse>> = _updateUserPasswordesult


    fun getUserDetail(){
        viewModelScope.launch {
            authRepository.getUserDetail().collect{
                _userDetailData.postValue(it)
            }
        }
    }

    fun updateProfile(image : MultipartBody.Part, name:String, email:String, phoneNumber:String, address:String, location:String, operationalHour:String){
        viewModelScope.launch {
            sellerRepository.updateSellerProfile(image, name, email, phoneNumber, address, location, operationalHour).collect{
                _updateProfileResult.postValue(it)
            }
        }
    }

    fun updateUserPassword(password:String) {
        viewModelScope.launch {
            authRepository.updateUserPassword(password).collect {
                _updateUserPasswordesult.postValue(it)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPrefRepository.clearPref()
        }
    }
}