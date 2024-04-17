package com.example.ta_mobile.ui.buyer.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.ta_mobile.data.repository.UserPrefRepository

class BuyerHomeViewModel(private val userPrefRepository: UserPrefRepository) : ViewModel() {

    fun getUserName() = userPrefRepository.getUserName().asLiveData()

}