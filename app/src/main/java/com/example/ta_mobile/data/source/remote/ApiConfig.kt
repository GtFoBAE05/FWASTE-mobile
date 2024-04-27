package com.example.ta_mobile.data.source.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val url = "http://192.168.1.4:4000/api/"
class ApiConfig(private val dataStore: DataStore<Preferences>) {
    fun getApiService(): ApiServices {

        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(dataStore))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


        return retrofit.create(ApiServices::class.java)
    }
}