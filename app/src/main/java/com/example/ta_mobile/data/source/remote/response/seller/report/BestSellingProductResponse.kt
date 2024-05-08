package com.example.ta_mobile.data.source.remote.response.seller.report

import com.google.gson.annotations.SerializedName

data class BestSellingProductResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<BestSellingProductResponseData>
)

data class BestSellingProductResponseData(
    @SerializedName("id")
    val id: String,
    @SerializedName("month")
    val month: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("total_sold")
    val totalSold: Int
)
