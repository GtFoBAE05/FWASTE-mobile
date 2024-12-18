package com.example.ta_mobile.ui.buyer.order.waiting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerOrderStatusWaitingBinding
import com.example.ta_mobile.ui.buyer.order.BuyerOrderStatusAdapter
import com.example.ta_mobile.ui.buyer.order.BuyerOrderStatusViewModel
import com.example.ta_mobile.utils.NetworkResult
import org.koin.android.ext.android.inject


class BuyerOrderStatusWaitingFragment : Fragment() {
    private var _binding : FragmentBuyerOrderStatusWaitingBinding? = null
    private val binding get() = _binding!!

    private val viewModel : BuyerOrderStatusViewModel by inject()
    private lateinit var adapter: BuyerOrderStatusAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerOrderStatusWaitingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getOrderStatus("waiting")
        setupAdapter()
        setupObserver()
    }

    private fun setupAdapter(){
        binding.buyerOrderStatusRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerOrderStatusAdapter {
            val bundle = Bundle()
            bundle.putString("transactionId", it.id)
            findNavController().navigate(
                R.id.action_buyerOrderStatusFragment_to_buyerOrderDetailFragment,
                bundle
            )
        }

        binding.buyerOrderStatusRv.adapter = adapter

    }

    private fun setupObserver(){
        viewModel.orderStatusResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {}
                NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    adapter.setData(it.data.data)
                }
            }
        }
    }


}