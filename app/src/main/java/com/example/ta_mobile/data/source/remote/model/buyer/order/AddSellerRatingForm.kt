package com.example.ta_mobile.data.source.remote.model.buyer.order

import com.google.gson.annotations.SerializedName

data class AddSellerRatingForm(
    @SerializedName("transaction_id")
    val transactionId: String,
    @SerializedName("rating")
    val rating: Float,
)
