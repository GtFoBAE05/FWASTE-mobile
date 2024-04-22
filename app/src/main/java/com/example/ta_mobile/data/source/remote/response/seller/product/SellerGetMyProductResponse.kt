package com.example.ta_mobile.data.source.remote.response.seller.product

import com.google.gson.annotations.SerializedName

data class SellerGetMyProductResponse(

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<SellerGetMyProductResponseData>
)

data class SellerGetMyProductResponseData(
    @SerializedName("id")
    val id: String,
    @SerializedName("store_id")
    val storeId: String,
    @SerializedName("store_name")
    val storeName: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("stock_count")
    val stockCount: Int,
    @SerializedName("rack_position")
    val rackPosition: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("is_visible")
    val isVisible: Boolean
)
