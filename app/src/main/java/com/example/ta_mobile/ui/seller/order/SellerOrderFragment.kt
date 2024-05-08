package com.example.ta_mobile.ui.seller.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ta_mobile.databinding.FragmentSellerOrderBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject

class SellerOrderFragment : Fragment() {

    private var _binding : FragmentSellerOrderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SellerOrderViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sellerOrderStatusToolbar.title = "Order List"
        val adapter = SellerOrderStatusViewPagerAdapter(childFragmentManager, lifecycle)
        binding.sellerOrderStatusViewPager.adapter = adapter

        TabLayoutMediator(binding.buyerOrderStatusTabs, binding.sellerOrderStatusViewPager){tab, pos ->
            when(pos){
                0 -> {
                    tab.text = "Waiting Confirmation"
                }
                1-> {
                    tab.text = "Active Order"
                }
                2-> {
                    tab.text = "Finished Order"
                }
                3 -> {
                    tab.text = "Canceled Order"
                }
            }

        }.attach()
    }


}