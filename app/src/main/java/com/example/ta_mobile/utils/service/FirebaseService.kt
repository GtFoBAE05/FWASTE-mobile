package com.example.ta_mobile.utils.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.asLiveData
import androidx.navigation.NavDeepLinkBuilder
import com.example.ta_mobile.R
import com.example.ta_mobile.data.repository.UserPrefRepository
import com.example.ta_mobile.ui.MainActivity
import com.example.ta_mobile.ui.buyer.BuyerActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"
class FirebaseService : FirebaseMessagingService() {

    private val dataStore: UserPrefRepository by inject()

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("TAG", "onMessageReceived: "+ message.data, )

        val bundle = Bundle().apply {
            putString("productId", message.data["productId"])
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random.nextInt()

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
//        val intent = Intent(this, BuyerActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            intent,
//            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//        )

        val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(BuyerActivity::class.java)
            .setGraph(R.navigation.buyer_navigation)
            .setDestination(R.id.buyerProductFragment)
            .setArguments(bundle)
            .createPendingIntent()

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        GlobalScope.launch {
//            val userRole = readPreferenceFromDataStore()
//            if (userRole == "buyer") {
//                notificationManager.notify(notificationID, notification)
//            }

            dataStore.getUserRole().collect {
                if (it == "buyer") {
                    notificationManager.notify(notificationID, notification)
                }
            }

        }
//        notificationManager.notify(notificationID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.GREEN
        }
        notificationManager.createNotificationChannel(channel)
    }

//    private suspend fun readPreferenceFromDataStore(): String {
//        val preferencesKey = stringPreferencesKey("userRole")
//        val preferences = dataStore.data.first()
//        return preferences[preferencesKey] ?: ""
//    }

}