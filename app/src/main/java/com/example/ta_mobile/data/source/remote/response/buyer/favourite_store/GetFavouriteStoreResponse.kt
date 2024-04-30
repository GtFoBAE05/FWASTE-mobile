package com.example.ta_mobile.data.source.remote.response.buyer.favourite_store

import com.google.gson.annotations.SerializedName

data class GetFavouriteStoreResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<GetFavouriteStoreResponseData>
)

data class GetFavouriteStoreResponseData(
    @SerializedName("id")
    val id: String,
    @SerializedName("store_id")
    val storeId: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("name")
    val fullname: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("operational_hour")
    val operationalHour: String,
    @SerializedName("rating")
    val rating: Int,
)
