package com.example.ta_mobile.ui.buyer.order

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ta_mobile.databinding.FragmentBuyerOrderStatusBinding
import com.google.android.material.tabs.TabLayoutMediator

class BuyerOrderStatusFragment : Fragment() {

    private var _binding: FragmentBuyerOrderStatusBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BuyerOrderStatusViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerOrderStatusBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buyerOrderStatusToolbar.title = "Order List"
        val adapter = BuyerOrderStatusViewPagerAdapter(childFragmentManager, lifecycle)
        binding.buyerOrderStatusViewPager.adapter = adapter

        TabLayoutMediator(binding.buyerOrderStatusTabs, binding.buyerOrderStatusViewPager){tab, pos ->
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