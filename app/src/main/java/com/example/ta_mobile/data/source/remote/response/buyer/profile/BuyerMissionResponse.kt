package com.example.ta_mobile.data.source.remote.response.buyer.profile

import com.google.gson.annotations.SerializedName

data class BuyerMissionResponse (

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<BuyerMissionResponseData>
)

data class BuyerMissionResponseData(
    @SerializedName("curent_transaction")
    val curentTransaction: Int,
    @SerializedName("transaction_needed")
    val transactionNeeded: Int,
    @SerializedName("point_reward")
    val pointReward: Int,
    @SerializedName("description")
    val description: String
)