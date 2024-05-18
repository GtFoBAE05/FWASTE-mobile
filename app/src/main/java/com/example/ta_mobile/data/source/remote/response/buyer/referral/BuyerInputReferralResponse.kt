package com.example.ta_mobile.data.source.remote.response.buyer.referral

import com.google.gson.annotations.SerializedName

data class BuyerInputReferralResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
)