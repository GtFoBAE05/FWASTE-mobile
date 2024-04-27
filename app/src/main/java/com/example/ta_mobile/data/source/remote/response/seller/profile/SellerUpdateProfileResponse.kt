package com.example.ta_mobile.data.source.remote.response.seller.profile

import com.google.gson.annotations.SerializedName

data class SellerUpdateProfileResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
)
