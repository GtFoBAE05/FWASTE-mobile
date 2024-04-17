package com.example.ta_mobile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ta_mobile.data.repository.UserPrefRepository

class MainViewModel(private val userPrefRepository: UserPrefRepository):ViewModel() {
    fun getUserType() = userPrefRepository.getUserRole().asLiveData()
}