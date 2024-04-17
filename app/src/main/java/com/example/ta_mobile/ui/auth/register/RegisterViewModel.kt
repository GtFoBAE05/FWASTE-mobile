package com.example.ta_mobile.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.AuthRepository
import com.example.ta_mobile.data.source.remote.response.auth.RegisterResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private var _registerResult = MutableLiveData<NetworkResult<RegisterResponse>>()
    val registerResult: LiveData<NetworkResult<RegisterResponse>> = _registerResult

    fun registerAsBuyer(name:String, email:String, password:String, phoneNumber:String, address:String, location:String){
        viewModelScope.launch {
            authRepository.registerAsBuyer(name, email, password, phoneNumber, address, location).collect{
                _registerResult.postValue(it)
            }
        }
    }

    fun registerAsSeller(name:String, email:String, password:String, phoneNumber:String, address:String, location:String, operationalHour:String){
        viewModelScope.launch {
            authRepository.registerAsSeller(name, email, password, phoneNumber, address, location, operationalHour).collect{
                _registerResult.postValue(it)
            }
        }
    }

}