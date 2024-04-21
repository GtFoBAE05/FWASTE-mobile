package com.example.ta_mobile.ui.seller

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.ActivityBuyerBinding
import com.example.ta_mobile.databinding.ActivitySellerBinding

class SellerActivity : AppCompatActivity() {


    private lateinit var binding : ActivitySellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivitySellerBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewSellerActivity) as NavHostFragment
        val navController = navHostFragment.navController


        binding.sellerBottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            if(destination.id == R.id.sellerHomeFragment || destination.id == R.id.sellerProductFragment || destination.id == R.id.sellerOrderFragment|| destination.id == R.id.sellerProfileFragment ){
                binding.sellerBottomNavigationView.visibility = View.VISIBLE
            }else{
                binding.sellerBottomNavigationView.visibility = View.GONE
            }
        }


    }
}