package com.example.ta_mobile.ui.buyer.order

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.lifecycle.Lifecycle

import com.example.ta_mobile.ui.buyer.order.active.BuyerOrderStatusActiveFragment
import com.example.ta_mobile.ui.buyer.order.canceled.BuyerOrderStatusCanceledFragment
import com.example.ta_mobile.ui.buyer.order.finished.BuyerOrderStatusFinishedFragment

class BuyerOrderStatusAdapter(fm : FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    private val fragmentList = listOf(BuyerOrderStatusActiveFragment(), BuyerOrderStatusFinishedFragment(),  BuyerOrderStatusCanceledFragment() )
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}