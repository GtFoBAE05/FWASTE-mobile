package com.example.ta_mobile.data.source.remote.model.buyer.order

import com.google.gson.annotations.SerializedName

data class BuyerAddOrderForm (

    @SerializedName("store_id")
    val storeId: String,
    @SerializedName("delivery_fee")
    val deliveryFee: Int,
    @SerializedName("service_fee")
    val serviceFee: Int,
    @SerializedName("user_voucher_used_id")
    val userVoucherUsedId: String = "",
    @SerializedName("dicount_amount")
    val dicountAmount: Int = 0,
    @SerializedName("subtotal")
    val subtotal: Int,
    @SerializedName("shipping_method")
    val shippingMethod: String,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("total_amount")
    val totalAmount: Int,
    @SerializedName("product")
    val product: List<BuyerAddOrderFormProduct>
)

data class BuyerAddOrderFormProduct(
    @SerializedName("product_id")
    val productId: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("unit_price")
    val unitPrice: Int,
    @SerializedName("product_subtotal")
    val productSubtotal: Int
)