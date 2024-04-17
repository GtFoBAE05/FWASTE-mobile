package com.example.ta_mobile.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.ta_mobile.data.repository.AuthRepository
import com.example.ta_mobile.data.repository.UserPrefRepository
import com.example.ta_mobile.data.source.local.user_preference.UserPreference
import com.example.ta_mobile.data.source.remote.ApiConfig
import com.example.ta_mobile.ui.MainViewModel
import com.example.ta_mobile.ui.auth.login.LoginViewModel
import com.example.ta_mobile.ui.auth.register.RegisterViewModel
import com.example.ta_mobile.ui.buyer.home.BuyerHomeViewModel
import com.example.ta_mobile.ui.buyer.profile.BuyerProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

val apiModule = module {
    single { ApiConfig(androidContext().dataStore).getApiService() }
}

val userPreferenceModule = module {
    single { UserPreference(androidContext().dataStore) }
}

val repositoryModule = module {
    single { AuthRepository(get(), get()) }
    single { UserPrefRepository(get()) }

}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }

    //auth
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }



    //buyer
    viewModel { BuyerHomeViewModel(get()) }
    viewModel { BuyerProfileViewModel(get()) }




    //seller
}