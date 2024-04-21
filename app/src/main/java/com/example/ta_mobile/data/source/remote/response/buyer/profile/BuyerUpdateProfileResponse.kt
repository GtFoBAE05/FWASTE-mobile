package com.example.ta_mobile.data.source.remote.response.buyer.profile

import com.google.gson.annotations.SerializedName

data class BuyerUpdateProfileResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
)