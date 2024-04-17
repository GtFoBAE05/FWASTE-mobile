package com.example.ta_mobile.data.source.remote.response.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
    val data: LoginData,

    @field:SerializedName("status")
    val status: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class LoginData(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("token")
    val accessToken: String
)