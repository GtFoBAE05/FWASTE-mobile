package com.example.ta_mobile.data.source.remote.response.buyer.order

import com.google.gson.annotations.SerializedName

data class AddOrderResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String
)