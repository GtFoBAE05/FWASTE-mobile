package com.example.ta_mobile.data.source.remote.response.buyer.store

import com.google.gson.annotations.SerializedName

data class SearchStoreResponse(

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<SearchStoreData>
)

data class SearchStoreData(
    @SerializedName("id")
    val id: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("operational_hour")
    val operationalHour: String,
    @SerializedName("rating")
    val rating: Double = 0.0
)