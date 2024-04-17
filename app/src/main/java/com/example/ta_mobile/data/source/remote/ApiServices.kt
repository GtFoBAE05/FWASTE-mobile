package com.example.ta_mobile.data.source.remote

import com.example.ta_mobile.data.source.remote.model.auth.BuyerRegisterForm
import com.example.ta_mobile.data.source.remote.model.auth.LoginForm
import com.example.ta_mobile.data.source.remote.model.auth.SellerRegisterForm
import com.example.ta_mobile.data.source.remote.response.auth.LoginResponse
import com.example.ta_mobile.data.source.remote.response.auth.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServices {

    //auth
    @POST("user/login")
    suspend fun login(
        @Body loginForm: LoginForm
    ): Response<LoginResponse>

    @POST("user/buyer/register")
    suspend fun registerAsBuyer(
        @Body buyerRegisterForm: BuyerRegisterForm
    ): Response<RegisterResponse>

    @POST("user/seller/register")
    suspend fun registerAsSeller(
        @Body sellerRegisterForm: SellerRegisterForm
    ): Response<RegisterResponse>
}