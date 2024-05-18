package com.example.ta_mobile.data.source.remote.response.auth

import com.google.gson.annotations.SerializedName

data class UserDetailResponse (

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: UserDetailResponseData
)

data class UserDetailResponseData(
    @SerializedName("id")
    val id: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("point")
    val point: Int,
    @SerializedName("balance")
    val balance: Int,
    @SerializedName("level")
    val level: String,
    @SerializedName("fullname")
    val fullname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("referal")
    val referal: String,
    @SerializedName("referal_filled")
    val referalFilled: Boolean,
    @SerializedName("address")
    val address: String,
    @SerializedName("location")
    val location: String,
    @SerializedName("operational_hour")
    val operationalHour: String,
    @SerializedName("rating")
    val rating: Float,
    @SerializedName("total_voucher")
    val totalVoucher: Int,
    @SerializedName("total_product")
    val totalProduct: Int,
    @SerializedName("total_price_saved")
    val totalPriceSaved: Int,
    @SerializedName("member_since")
    val member_since: String
)
