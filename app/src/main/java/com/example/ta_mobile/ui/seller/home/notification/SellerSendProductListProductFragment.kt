package com.example.ta_mobile.ui.seller.home.notification

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentSellerSendProductListProductBinding
import com.example.ta_mobile.ui.seller.home.SellerHomeViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject

class SellerSendProductListProductFragment : Fragment() {


    private var _binding: FragmentSellerSendProductListProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SellerHomeViewModel by inject()

    private lateinit var adapter: SellerSendNotificationProductAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentSellerSendProductListProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sellerSendProductListProductToolbar.title = "Send Product"
        binding.sellerSendProductListProductToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getMyProduct()
        setupAdapter()
        setupObserver()

    }

    private fun setupAdapter() {
        binding.rvSellerSendProductListProduct.layoutManager = LinearLayoutManager(requireContext())
        adapter = SellerSendNotificationProductAdapter {
            val bundle = Bundle()
            bundle.putString("productId", it.id)
            bundle.putString("productName", it.name)
            bundle.putString("imageUrl", it.imageUrl)
            setFragmentResult("product", bundle)
            findNavController().popBackStack()
        }
        binding.rvSellerSendProductListProduct.adapter = adapter

    }

    private fun setupObserver() {
        viewModel.myProductData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    showToast(it.error)
                    binding.sellerSendProductListProductprogressBar.gone()
                }

                NetworkResult.Loading -> {
                    binding.sellerSendProductListProductprogressBar.visible()
                }

                is NetworkResult.Success -> {
                    binding.sellerSendProductListProductprogressBar.gone()
                    adapter.setData(it.data.data)
                }
            }
        }
    }
}