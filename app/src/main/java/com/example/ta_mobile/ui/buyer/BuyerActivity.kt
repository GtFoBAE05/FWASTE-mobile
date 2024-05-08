package com.example.ta_mobile.ui.buyer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
            if(destination.id == R.id.buyerHomeFragment || destination.id == R.id.buyerNearestStoreFragment ||destination.id == R.id.buyerOrderStatusFragment || destination.id == R.id.buyerCartFragment|| destination.id == R.id.buyerProfileFragment ){
                binding.buyerBottomNavigationView.visibility = View.VISIBLE
            }else{
                binding.buyerBottomNavigationView.visibility = View.GONE
            }
        }

//        val extras = intent.getStringExtra("productId")
//        if (extras != null) {
//            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//            val fragment = BuyerProductFragment().apply {
//                arguments = Bundle().apply {
//                    putString("productId", extras)
//                }
//            }
//            fragmentTransaction.replace(R.id.fragmentContainerViewBuyerActivity, fragment)
//                .addToBackStack(null)
//                .commit()
//
//        }

    }
}