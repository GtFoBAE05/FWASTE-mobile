package com.example.ta_mobile.ui.seller.order.finished

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentSellerFinishedOrderBinding
import com.example.ta_mobile.ui.seller.order.SellerOrderStatusAdapter
import com.example.ta_mobile.ui.seller.order.SellerOrderViewModel
import com.example.ta_mobile.utils.NetworkResult
import org.koin.android.ext.android.inject


class SellerFinishedOrderFragment : Fragment() {

    private var _binding : FragmentSellerFinishedOrderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SellerOrderViewModel by inject()
    private lateinit var adapter: SellerOrderStatusAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerFinishedOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getOrderStatus("finished")
        setupAdapter()
        setupObserver()
    }

    private fun setupAdapter(){
        binding.sellerFinishedOrderRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SellerOrderStatusAdapter {
            val bundle = Bundle()
            bundle.putString("transactionId", it.id)
            findNavController().navigate(
                R.id.action_sellerOrderFragment_to_sellerDetailOrderFragment,
                bundle
            )
        }

        binding.sellerFinishedOrderRv.adapter = adapter

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