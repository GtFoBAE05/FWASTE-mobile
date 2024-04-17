package com.example.ta_mobile.data.repository

import com.example.ta_mobile.data.source.local.user_preference.UserPreference
import com.example.ta_mobile.data.source.local.user_preference.UserPreferenceClass

class UserPrefRepository(
    private val userPreference: UserPreference
) {
    fun getUserId() = userPreference.getUserId()
    fun getUserRole() = userPreference.getUserRole()
    fun getUserName() = userPreference.getUserName()
    fun getTokenAccess() = userPreference.getTokenAccess()

    suspend fun saveTokenAccess(tokenAccess:String) = userPreference.saveTokenAccess(tokenAccess)

    suspend fun saveUserPref(user : UserPreferenceClass) = userPreference.saveUserPref(user)

    suspend fun clearTokenAccess() = userPreference.clearTokenAccess()
    suspend fun clearPref() = userPreference.clear()
}