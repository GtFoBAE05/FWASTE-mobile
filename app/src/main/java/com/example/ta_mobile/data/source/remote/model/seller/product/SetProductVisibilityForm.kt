package com.example.ta_mobile.data.source.remote.model.seller.product

import com.google.gson.annotations.SerializedName

data class SetProductVisibilityForm(
    @SerializedName("product_id")
    val product_id: String,
    @SerializedName("status")
    val status: Boolean,
)
