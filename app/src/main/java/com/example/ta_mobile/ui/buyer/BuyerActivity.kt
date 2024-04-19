package com.example.ta_mobile.ui.buyer

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.ActivityBuyerBinding

class BuyerActivity : AppCompatActivity() {


    private lateinit var binding : ActivityBuyerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewBuyerActivity) as NavHostFragment
        val navController = navHostFragment.navController


        binding.buyerBottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if(destination.id == R.id.buyerHomeFragment || destination.id == R.id.buyerCartFragment|| destination.id == R.id.buyerProfileFragment ){
                binding.buyerBottomNavigationView.visibility = View.VISIBLE
            }else{
                binding.buyerBottomNavigationView.visibility = View.GONE
            }
        }

    }
}