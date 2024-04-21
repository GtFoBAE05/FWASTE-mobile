package com.example.ta_mobile.ui.seller.order

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ta_mobile.R

class SellerOrderFragment : Fragment() {

    companion object {
        fun newInstance() = SellerOrderFragment()
    }

    private val viewModel: SellerOrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_seller_order, container, false)
    }
}