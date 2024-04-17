package com.example.ta_mobile.di

import android.app.Application
import androidx.core.provider.FontRequest
import com.example.ta_mobile.R
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                apiModule,
                userPreferenceModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}