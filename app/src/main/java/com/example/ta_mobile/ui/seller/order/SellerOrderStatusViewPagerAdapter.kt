package com.example.ta_mobile.ui.seller.order

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.lifecycle.Lifecycle

import com.example.ta_mobile.ui.buyer.order.active.BuyerOrderStatusActiveFragment
import com.example.ta_mobile.ui.buyer.order.rejected.BuyerOrderStatusRejectedFragment
import com.example.ta_mobile.ui.buyer.order.finished.BuyerOrderStatusFinishedFragment
import com.example.ta_mobile.ui.buyer.order.waiting.BuyerOrderStatusWaitingFragment
import com.example.ta_mobile.ui.seller.order.active.SellerActiveOrderFragment
import com.example.ta_mobile.ui.seller.order.canceled.SellerCanceledOrderFragment
import com.example.ta_mobile.ui.seller.order.finished.SellerFinishedOrderFragment
import com.example.ta_mobile.ui.seller.order.waiting.SellerWaitingOrderFragment

class SellerOrderStatusViewPagerAdapter(fm : FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    private val fragmentList = listOf(SellerWaitingOrderFragment(), SellerActiveOrderFragment(), SellerFinishedOrderFragment() ,SellerCanceledOrderFragment() )
    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}