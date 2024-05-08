package com.example.ta_mobile.ui.buyer.order

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.ta_mobile.ui.buyer.order.active.BuyerOrderStatusActiveFragment
import com.example.ta_mobile.ui.buyer.order.finished.BuyerOrderStatusFinishedFragment
import com.example.ta_mobile.ui.buyer.order.rejected.BuyerOrderStatusRejectedFragment
import com.example.ta_mobile.ui.buyer.order.waiting.BuyerOrderStatusWaitingFragment

class BuyerOrderStatusViewPagerAdapter(fm : FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    private val fragmentList = listOf(BuyerOrderStatusWaitingFragment(), BuyerOrderStatusActiveFragment(), BuyerOrderStatusFinishedFragment(),  BuyerOrderStatusRejectedFragment() )
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}