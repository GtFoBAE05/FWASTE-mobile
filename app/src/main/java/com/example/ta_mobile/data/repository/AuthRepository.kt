package com.example.ta_mobile.data.repository

import android.util.Log
import com.example.ta_mobile.data.source.local.user_preference.UserPreference
import com.example.ta_mobile.data.source.local.user_preference.UserPreferenceClass
import com.example.ta_mobile.data.source.remote.ApiServices
import com.example.ta_mobile.data.source.remote.model.auth.BuyerRegisterForm
import com.example.ta_mobile.data.source.remote.model.auth.LoginForm
import com.example.ta_mobile.data.source.remote.model.auth.SellerRegisterForm
import com.example.ta_mobile.data.source.remote.response.auth.LoginResponse
import com.example.ta_mobile.data.source.remote.response.auth.RegisterResponse
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.exception.NoDataException
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException

class AuthRepository(
    private val userPreference: UserPreference,
    private val apiServices: ApiServices,
) {
    suspend fun login(
        email: String,
        password: String
    ): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val loginResponse = apiServices.login(LoginForm(email, password))
                if (loginResponse.isSuccessful) {
                    val result = loginResponse.body()!!.data
                    userPreference.saveUserPref(
                        UserPreferenceClass(
                            result.id, result.role, result.name, result.accessToken
                        )
                    )
                    emit(
                        NetworkResult.Success(
                            loginResponse.body() ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    loginResponse.errorBody()?.let {
                        val error = JSONObject(it.string())
                        emit(NetworkResult.Error(error.getString("message")))
                    }
                }
            } catch (e: HttpException) {
                Log.e("AuthRepository", "login HttpException: " + e.message)
                emit(NetworkResult.Error("Request Failed: ${e.message.toString()}"))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun registerAsBuyer(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        address: String,
        location: String
    ): Flow<NetworkResult<RegisterResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val registerResponse = apiServices.registerAsBuyer(
                    BuyerRegisterForm(
                        name,
                        email,
                        password,
                        phoneNumber,
                        address,
                        location
                    )
                )
                if (registerResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            registerResponse.body() ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    registerResponse.errorBody()?.let {
                        val error = JSONObject(it.string())
                        emit(NetworkResult.Error(error.getString("message")))
                    }
                }
            } catch (e: HttpException) {
                Log.e("AuthRepository", "register HttpException: " + e.message)
                emit(NetworkResult.Error("Request Failed: ${e.message.toString()}"))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun registerAsSeller(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        address: String,
        location: String,
        operationalHour:String
    ): Flow<NetworkResult<RegisterResponse>> {
        return flow {
            emit(NetworkResult.Loading)
            try {
                val registerResponse = apiServices.registerAsSeller(
                    SellerRegisterForm(
                        name,
                        email,
                        password,
                        phoneNumber,
                        address,
                        location,
                        operationalHour
                    )
                )
                if (registerResponse.isSuccessful) {
                    emit(
                        NetworkResult.Success(
                            registerResponse.body() ?: throw NoDataException("No data found")
                        )
                    )
                } else {
                    registerResponse.errorBody()?.let {
                        val error = JSONObject(it.string())
                        emit(NetworkResult.Error(error.getString("message")))
                    }
                }
            } catch (e: HttpException) {
                Log.e("AuthRepository", "register HttpException: " + e.message)
                emit(NetworkResult.Error("Request Failed: ${e.message.toString()}"))
            } catch (e: Exception) {
                emit(NetworkResult.Error(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }


}