package com.example.ta_mobile.di

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.ta_mobile.data.repository.AuthRepository
import com.example.ta_mobile.data.repository.BuyerRepository
import com.example.ta_mobile.data.repository.UserPrefRepository
import com.example.ta_mobile.data.source.local.db.CartProductDatabase
import com.example.ta_mobile.data.source.local.db.dao.CartProductDao
import com.example.ta_mobile.data.source.local.user_preference.UserPreference
import com.example.ta_mobile.data.source.remote.ApiConfig
import com.example.ta_mobile.ui.MainViewModel
import com.example.ta_mobile.ui.auth.login.LoginViewModel
import com.example.ta_mobile.ui.auth.register.RegisterViewModel
import com.example.ta_mobile.ui.buyer.cart.BuyerCartViewModel
import com.example.ta_mobile.ui.buyer.checkout.BuyerCheckoutFragment
import com.example.ta_mobile.ui.buyer.checkout.BuyerCheckoutViewModel
import com.example.ta_mobile.ui.buyer.home.BuyerHomeViewModel
import com.example.ta_mobile.ui.buyer.product.BuyerProductViewModel
import com.example.ta_mobile.ui.buyer.profile.BuyerProfileViewModel
import com.example.ta_mobile.ui.buyer.search.BuyerSearchStoreViewModel
import com.example.ta_mobile.ui.buyer.store.BuyerStoreDetailViewModel
import com.example.ta_mobile.utils.Session
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.dsl.module

private val Context.dataStore by preferencesDataStore(name = "user_preferences")

private fun provideDatabase(application: Application): CartProductDatabase {
    return Room.databaseBuilder(
        application,
        CartProductDatabase::class.java,
        "cart_product_database"
    ).fallbackToDestructiveMigration().build()
}

private fun provideDao(cartProductDatabase: CartProductDatabase) : CartProductDao = cartProductDatabase.dao()

val apiModule = module {
    single { ApiConfig(androidContext().dataStore).getApiService() }
}

val userPreferenceModule = module {
    single { UserPreference(androidContext().dataStore) }
}

val roomModule = module {
    single { provideDatabase(get()) }
    single { provideDao(get()) }
}

val repositoryModule = module {
    single { AuthRepository(get(), get()) }
    single { UserPrefRepository(get()) }
    single { BuyerRepository(get(), get()) }

}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }

    //auth
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }



    //buyer
    viewModel { BuyerHomeViewModel(get(), get()) }
    viewModel { BuyerCartViewModel(get()) }
    viewModel { BuyerProfileViewModel(get()) }


    viewModel { BuyerSearchStoreViewModel(get())}
    viewModel { BuyerStoreDetailViewModel(get()) }
    viewModel { BuyerProductViewModel(get()) }

//    viewModelOf(::BuyerCheckoutViewModel)
//    scope<BuyerCheckoutViewModel> {
//        scopedOf(::Session)
//    }

    viewModel { BuyerCheckoutViewModel(get()) }




    //seller
}