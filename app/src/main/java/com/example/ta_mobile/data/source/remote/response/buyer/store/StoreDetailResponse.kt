package com.example.ta_mobile.data.source.remote.response.buyer.store

import com.google.gson.annotations.SerializedName

data class StoreDetailResponse(

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: StoreDetailResponseData
)

data class StoreDetailResponseData(
    @SerializedName("store_id")
    val storeId: String,
    @SerializedName("store_name")
    val storeName: String,
    @SerializedName("store_image_url")
    val storeImageUrl: String,
    @SerializedName("store_address")
    val storeAddress: String,
    @SerializedName("store_operational_hour")
    val storeOperationalHour: String,
    @SerializedName("products")
    val products: List<StoreDetailResponseProductsData>
)

data class StoreDetailResponseProductsData(
    @SerializedName("id")
    val id: String,
    @SerializedName("store_id")
    val storeId: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("original_price")
    val originalPrice: Int,
    @SerializedName("stock_count")
    val stockCount: Int,
    @SerializedName("rack_position")
    val rackPosition: String,
    @SerializedName("is_visible")
    val isVisible: Boolean
)