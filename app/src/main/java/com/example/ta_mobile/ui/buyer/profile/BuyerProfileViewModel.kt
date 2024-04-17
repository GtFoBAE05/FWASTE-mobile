package com.example.ta_mobile.ui.buyer.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.repository.UserPrefRepository
import kotlinx.coroutines.launch

class BuyerProfileViewModel(
    private val userPrefRepository: UserPrefRepository,
//    private val buyerRepository: BuyerRepository
) : ViewModel() {


    fun logout() {
        viewModelScope.launch {
            userPrefRepository.clearPref()
        }
    }

}