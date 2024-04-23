package com.example.ta_mobile.data.source.remote.response.seller.product

import com.google.gson.annotations.SerializedName

data class SellerGetMySingleProductResponse (

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: SellerGetMySingleProductData
)

data class SellerGetMySingleProductData(
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
    val products: SellerGetMySingleProductDataProducts
)

data class SellerGetMySingleProductDataProducts(
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