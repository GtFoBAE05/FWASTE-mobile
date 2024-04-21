package com.example.ta_mobile.data.source.remote.response.buyer.profile

import com.google.gson.annotations.SerializedName

data class BuyerPointResponse (

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: BuyerPointResponseData
)

data class BuyerPointResponseData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("level_name")
    val levelName: String,
    @SerializedName("point_minimum")
    val pointMinimum: Int,
    @SerializedName("user_point")
    val userPoint: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("point_to_next_level")
    val pointToNextLevel: Int,
    @SerializedName("voucher_reward")
    val voucherReward: List<BuyerPointResponseDataVoucherReward>
)

data class BuyerPointResponseDataVoucherReward(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String
)