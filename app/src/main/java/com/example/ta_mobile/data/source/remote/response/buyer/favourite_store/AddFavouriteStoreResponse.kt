package com.example.ta_mobile.data.source.remote.response.buyer.favourite_store

import com.google.gson.annotations.SerializedName

data class AddFavouriteStoreResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
)
