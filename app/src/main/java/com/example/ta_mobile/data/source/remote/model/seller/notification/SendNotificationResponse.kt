package com.example.ta_mobile.data.source.remote.model.seller.notification

import com.google.gson.annotations.SerializedName

data class SendNotificationForm(

    @SerializedName("title")
    val title: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("product_id")
    val productid: String
)
