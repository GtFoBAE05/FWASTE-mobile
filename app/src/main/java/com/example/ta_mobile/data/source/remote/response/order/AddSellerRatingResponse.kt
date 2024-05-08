package com.example.ta_mobile.data.source.remote.response.order

import com.google.gson.annotations.SerializedName

data class AddSellerRatingResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String
)