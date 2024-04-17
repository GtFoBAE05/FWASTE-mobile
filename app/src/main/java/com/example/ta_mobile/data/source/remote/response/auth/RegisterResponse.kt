package com.example.ta_mobile.data.source.remote.response.auth

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("status")
    var status: Boolean,
    @SerializedName("message")
    val message: String
)