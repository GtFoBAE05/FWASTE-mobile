package com.example.ta_mobile.ui.buyer.order.active

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerOrderStatusActiveBinding
import com.example.ta_mobile.ui.buyer.order.BuyerOrderStatusViewModel
import org.koin.android.ext.android.inject


class BuyerOrderStatusActiveFragment : Fragment() {

    private var _binding : FragmentBuyerOrderStatusActiveBinding? = null
    private val binding get() = _binding!!

    private val viewModel : BuyerOrderStatusViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBuyerOrderStatusActiveBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }




}