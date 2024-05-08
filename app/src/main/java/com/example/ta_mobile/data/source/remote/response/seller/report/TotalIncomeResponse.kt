package com.example.ta_mobile.data.source.remote.response.seller.report

import com.google.gson.annotations.SerializedName

data class TotalIncomeResponse (

    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<TotalIncomeResponseData>
)

data class TotalIncomeResponseData(
    @SerializedName("month")
    val month: String,
    @SerializedName("income")
    val income: Int
)