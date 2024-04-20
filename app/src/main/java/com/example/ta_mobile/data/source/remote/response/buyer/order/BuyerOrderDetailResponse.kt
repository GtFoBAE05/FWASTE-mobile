package com.example.ta_mobile.data.source.remote.response.buyer.order

import com.google.gson.annotations.SerializedName

data class BuyerOrderDetailResponse (

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: BuyerOrderDetailResponseData
)

data class BuyerOrderDetailResponseData(
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
    @SerializedName("shipping_method")
    val shippingMethod: String,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("is_rated")
    val isRated: Boolean,
    @SerializedName("order")
    val order: BuyerOrderDetailResponseDataOrder
)

data class BuyerOrderDetailResponseDataOrder(
    @SerializedName("id")
    val id: String,
    @SerializedName("subtotal")
    val subtotal: Int,
    @SerializedName("delivery_fee")
    val deliveryFee: Int,
    @SerializedName("service_fee")
    val serviceFee: Int,
    @SerializedName("user_voucher_used_id")
    val userVoucherUsedId: String,
    @SerializedName("dicount_amount")
    val discountAmount: Int = 0,
    @SerializedName("order_status")
    val orderStatus: String,
    @SerializedName("product")
    val product: List<BuyerOrderDetailResponseDataProduct>
)

data class BuyerOrderDetailResponseDataProduct(
    @SerializedName("id")
    val id: String,
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("product_image_url")
    val productImageUrl: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("unit_price")
    val unitPrice: Int,
    @SerializedName("subtotal")
    val subtotal: Int
)