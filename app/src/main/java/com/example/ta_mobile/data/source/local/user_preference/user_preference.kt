package com.example.ta_mobile.data.source.local.user_preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference(private val dataStore: DataStore<Preferences>) {

    private val USER_ID = stringPreferencesKey("ID")
    private val USER_ROLE = stringPreferencesKey("userRole")
    private val USER_NAME = stringPreferencesKey("userName")
    private val TOKEN_ACCESS_KEY = stringPreferencesKey("tokenAccess")

    fun getUserId(): Flow<String> {
        return dataStore.data.map {
            it[USER_ID] ?: ""
        }
    }

    fun getUserRole(): Flow<String> {
        return dataStore.data.map {
            it[USER_ROLE] ?: ""
        }
    }

    fun getUserName(): Flow<String> {
        return dataStore.data.map {
            it[USER_NAME] ?: ""
        }
    }

    fun getTokenAccess(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN_ACCESS_KEY] ?: ""
        }
    }

    suspend fun saveTokenAccess(tokenAccess: String) {
        dataStore.edit {
            it[TOKEN_ACCESS_KEY] = tokenAccess
        }
    }

    suspend fun saveUserPref(user: UserPreferenceClass) {
        dataStore.edit {
            it[USER_ID] = user.userId
            it[USER_ROLE] = user.userRole
            it[USER_NAME] = user.userName
            it[TOKEN_ACCESS_KEY] = user.tokenAccess
        }
    }

    suspend fun clearTokenAccess() {
        dataStore.edit {
            it[TOKEN_ACCESS_KEY] = ""
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

}

data class UserPreferenceClass(
    val userId: String,
    val userRole: String,
    val userName: String,
    val tokenAccess: String,
)