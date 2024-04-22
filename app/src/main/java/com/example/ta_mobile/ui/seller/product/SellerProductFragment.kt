package com.example.ta_mobile.ui.seller.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentSellerProductBinding
import com.example.ta_mobile.ui.seller.home.notification.SellerSendNotificationProductAdapter
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject


class SellerProductFragment : Fragment() {

    private var _binding: FragmentSellerProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel : SellerProductViewModel by inject()

    private lateinit var adapter: SellerProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sellerProductToolbar.title = "My Product"

        viewModel.getMyProduct()

        setupObserver()
        setupAdapter()
        setupButton()
    }

    private fun setupButton(){

    }

    private fun setupAdapter() {
        binding.sellerProductRv.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = SellerProductAdapter {

        }
        binding.sellerProductRv.adapter = adapter

    }

    private fun setupObserver() {
        viewModel.myProductData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    showToast(it.error)
                    binding.sellerProductprogressBar.gone()
                }

                NetworkResult.Loading -> {
                    binding.sellerProductprogressBar.visible()
                }

                is NetworkResult.Success -> {
                    binding.sellerProductprogressBar.gone()
                    adapter.setData(it.data.data)
                }
            }
        }
    }


}