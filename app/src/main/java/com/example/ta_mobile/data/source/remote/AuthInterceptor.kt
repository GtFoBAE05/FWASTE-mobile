package com.example.ta_mobile.data.source.remote

import android.content.ContentValues
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.ta_mobile.data.repository.AuthRepository
import com.example.ta_mobile.data.repository.UserPrefRepository
import com.example.ta_mobile.data.source.local.user_preference.UserPreference
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val dataStore: DataStore<Preferences>) : Interceptor {

    private lateinit var response: Response

    private val apiService by lazy {
        ApiConfig(dataStore).getApiService()
    }
    private val userPreference by lazy { UserPreference(dataStore) }

    private val authRepository by lazy {
        AuthRepository(userPreference, apiService)
    }

    private val userPrefRepository by lazy {
        UserPrefRepository(userPreference)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val token = runBlocking {
            dataStore.data.first()[stringPreferencesKey("tokenAccess")]
        }

        if (!token.isNullOrEmpty()) {
            val request = original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()

            response = chain.proceed(request)

//            if (response.code == 401) {

//                response.close()

//                val tokenRefresh = runBlocking {
//                    dataStore.data.first()[stringPreferencesKey("tokenRefresh")]
//                }

//                runBlocking {
//                    authRepository.refreshToken(tokenRefresh.toString()).collect {
//                        when (it) {
//                            is Resource.Success -> {
//                                userPrefRepository.saveTokenAccess(it.data.tokenAccess.toString())
//                            }
//
//                            is Resource.Loading -> {
//
//                            }
//
//                            is Resource.Error -> {
//                                Log.e(ContentValues.TAG, "error refresh token in authInterceptor: " + it.error)
//                            }
//                        }
//                    }
//                }

//                val newToken = runBlocking {
//                    dataStore.data.first()[stringPreferencesKey("tokenAccess")]
//                }
//
//                val newRequest = chain.request().newBuilder()
//                    .addHeader("Authorization", "Bearer $newToken")
//                    .build()
//
//                response = chain.proceed(newRequest)
//
//            }


        } else {
            response = chain.proceed(original)
        }

        return response


    }

}