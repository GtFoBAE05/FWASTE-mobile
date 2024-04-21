package com.example.ta_mobile.data.source.remote.response.auth

import com.google.gson.annotations.SerializedName

data class UpdatePasswordResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,)
