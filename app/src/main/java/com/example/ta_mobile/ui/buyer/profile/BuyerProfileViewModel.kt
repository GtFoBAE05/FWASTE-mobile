package com.example.ta_mobile.ui.buyer.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.AuthRepository
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.repository.UserPrefRepository
import com.example.ta_mobile.data.source.remote.response.auth.UpdatePasswordResponse
import com.example.ta_mobile.data.source.remote.response.auth.UserDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerMissionResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerPointResponse
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerUpdateProfileResponse
import com.example.ta_mobile.data.source.remote.response.buyer.voucher.UserOwnedVoucherResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class BuyerProfileViewModel(
    private val userPrefRepository: UserPrefRepository,
    private val authRepository: AuthRepository,
    private val buyerRepository: BuyerRepository
) : ViewModel() {

    private var _userDetailData = MutableLiveData<NetworkResult<UserDetailResponse>>()
    val userDetailData : LiveData<NetworkResult<UserDetailResponse>> = _userDetailData

    private var _userVoucherList = MutableLiveData<NetworkResult<UserOwnedVoucherResponse>>()
    val userVoucherList: LiveData<NetworkResult<UserOwnedVoucherResponse>> = _userVoucherList

    private var _userPointDetail = MutableLiveData<NetworkResult<BuyerPointResponse>>()
    val userPointDetail: LiveData<NetworkResult<BuyerPointResponse>> = _userPointDetail

    private var _userMission = MutableLiveData<NetworkResult<BuyerMissionResponse>>()
    val userMission: LiveData<NetworkResult<BuyerMissionResponse>> = _userMission

    private var _updateProfileResult = MutableLiveData<NetworkResult<BuyerUpdateProfileResponse>>()
    val updateProfileResult: LiveData<NetworkResult<BuyerUpdateProfileResponse>> = _updateProfileResult

    private var _updateUserPasswordesult = MutableLiveData<NetworkResult<UpdatePasswordResponse>>()
    val updateUserPasswordesult: LiveData<NetworkResult<UpdatePasswordResponse>> = _updateUserPasswordesult

    fun getUserDetail(){
        viewModelScope.launch {
            authRepository.getUserDetail().collect{
                _userDetailData.postValue(it)
            }
        }
    }

    fun getUserOwnedVoucher() {
        viewModelScope.launch {
            buyerRepository.getUserOwnedVoucher().collect {
                _userVoucherList.postValue(it)
            }
        }
    }

    fun getBuyerPointDetail() {
        viewModelScope.launch {
            buyerRepository.getBuyerPointDetail().collect {
                _userPointDetail.postValue(it)
            }
        }
    }

    fun getBuyerMission() {
        viewModelScope.launch {
            buyerRepository.getBuyerMission().collect {
                _userMission.postValue(it)
            }
        }
    }

    fun updateProfile(image : MultipartBody.Part, name:String, email:String, phoneNumber:String, address:String, location:String){
        viewModelScope.launch {
            buyerRepository.updateBuyerProfile(image, name, email, phoneNumber, address, location).collect{
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