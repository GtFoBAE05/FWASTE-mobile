package com.example.ta_mobile.ui.seller.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentSellerProductBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
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
    ): View {
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
        binding.sellerProductfloatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_sellerProductFragment_to_sellerAddProductFragment)
        }

    }

    private fun setupAdapter() {
        binding.sellerProductRv.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = SellerProductAdapter {
            val bundle = Bundle()
            bundle.putString("productId", it.id)
            findNavController().navigate(R.id.action_sellerProductFragment_to_sellerDetailProductFragment, bundle)
        }
        binding.sellerProductRv.adapter = adapter

    }

    private fun setupObserver() {
        viewModel.myProductData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
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