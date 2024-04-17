package com.example.ta_mobile.data.source.remote.model.auth

import com.google.gson.annotations.SerializedName

data class BuyerRegisterForm(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("location")
    val location: String,
)