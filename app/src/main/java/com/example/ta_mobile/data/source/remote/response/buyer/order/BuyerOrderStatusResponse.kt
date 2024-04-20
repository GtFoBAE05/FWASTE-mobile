package com.example.ta_mobile.data.source.remote.response.buyer.order

import com.google.gson.annotations.SerializedName

data class BuyerOrderStatusResponse(

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<BuyerOrderStatusResponseData>
)

data class BuyerOrderStatusResponseData(
    @SerializedName("id")
    val id: String,
    @SerializedName("store_id")
    val storeId: String,
    @SerializedName("store_image_url")
    val storeImageUrl: String,
    @SerializedName("store_name")
    val storeName: String,
    @SerializedName("user_name")
    val userName: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("order_id")
    val orderId: String,
    @SerializedName("total_amount")
    val totalAmount: Int,
    @SerializedName("is_rated")
    val isRated: Boolean
)