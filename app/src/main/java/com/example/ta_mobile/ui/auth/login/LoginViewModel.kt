package com.example.ta_mobile.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.AuthRepository
import com.example.ta_mobile.data.repository.UserPrefRepository
import com.example.ta_mobile.data.source.local.user_preference.UserPreferenceClass
import com.example.ta_mobile.data.source.remote.response.auth.LoginResponse
import com.example.ta_mobile.utils.NetworkResult
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPrefRepository: UserPrefRepository
) : ViewModel() {
    private var _userLogin = MutableLiveData<NetworkResult<LoginResponse>>()
    val userLogin: LiveData<NetworkResult<LoginResponse>> = _userLogin

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authRepository.login(email, password).collect {
                _userLogin.postValue(it)
            }
        }
    }

}