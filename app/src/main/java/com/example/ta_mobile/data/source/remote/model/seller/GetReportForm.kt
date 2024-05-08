package com.example.ta_mobile.data.source.remote.model.seller

import com.google.gson.annotations.SerializedName

data class GetReportForm(
    @SerializedName("start_month")
    val startMonth: Int,
    @SerializedName("end_month")
    val endMonth: Int,
    @SerializedName("start_year")
    val startYear: Int ,
    @SerializedName("end_year")
    val endYear: Int
)
