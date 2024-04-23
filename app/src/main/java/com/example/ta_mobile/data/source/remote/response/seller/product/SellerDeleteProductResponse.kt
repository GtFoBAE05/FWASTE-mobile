package com.example.ta_mobile.data.source.remote.response.seller.product

import com.google.gson.annotations.SerializedName

data class SellerDeleteProductResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
)