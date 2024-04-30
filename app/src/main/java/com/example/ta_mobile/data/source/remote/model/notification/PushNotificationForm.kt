package com.example.ta_mobile.data.source.remote.model.notification

data class PushNotificationForm (
    val data: PushNotificationFormNotificationData,
    val to: String

)

data class PushNotificationFormNotificationData(
    val title: String,
    val message: String
)