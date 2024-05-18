package com.example.ta_mobile.data.source.remote.response.buyer.voucher

import com.google.gson.annotations.SerializedName

data class UserOwnedVoucherResponse(

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<UserOwnedVoucherResponseData>
){
    init {
        data.forEach {
            it.isSelected = true
        }
    }
}

data class UserOwnedVoucherResponseData(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("expire_at")
    val expireAt: String,
    var isSelected : Boolean = true
)

data class UserOwnedVoucherResponseDataMappedWithIsSelected(
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val amount: Int,
    val expireAt: String,
    val isSelected : Boolean = true
)