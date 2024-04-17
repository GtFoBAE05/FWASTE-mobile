package com.example.ta_mobile.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.ActivityMainBinding
import com.example.ta_mobile.ui.auth.AuthActivity
import com.example.ta_mobile.ui.buyer.BuyerActivity
import com.example.ta_mobile.ui.seller.SellerActivity
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val mainViewModel : MainViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        splashScreen.setKeepOnScreenCondition { false }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainViewModel.getUserType().observe(this){
            if(it.isNullOrEmpty()){
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }else{
                if(it.equals("buyer")){
                    startActivity(Intent(this, BuyerActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this, SellerActivity::class.java))
                    finish()
                }

            }
        }



    }
}