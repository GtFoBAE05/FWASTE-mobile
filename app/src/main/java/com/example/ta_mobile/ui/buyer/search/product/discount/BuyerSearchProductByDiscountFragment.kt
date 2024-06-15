package com.example.ta_mobile.ui.buyer.search.product.discount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerSearchProductBinding
import com.example.ta_mobile.databinding.FragmentBuyerSearchProductByDiscountBinding
import com.example.ta_mobile.ui.buyer.search.product.BuyerSearchProductAdapter
import com.example.ta_mobile.ui.buyer.search.product.category.BuyerSearchProductViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject


class BuyerSearchProductByDiscountFragment : Fragment() {

    private lateinit var _binding: FragmentBuyerSearchProductByDiscountBinding
    private val binding get() = _binding

    private val viewModel: BuyerSearchProductByDiscountViewModel by inject()

    private lateinit var keyword:String


    private lateinit var adapter: BuyerSearchProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = requireArguments().getString("keyword").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerSearchProductByDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buyerSearchProductToolbar.title = "more than " + keyword + "% off"
        binding.buyerSearchProductToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.searchProductResult(keyword)
        setupAdapter()
        setupObserver()

    }

    private fun setupAdapter(){
        binding.buyerSearchProductRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerSearchProductAdapter {
            val bundle = Bundle()
            bundle.putString("productId", it.id)
            findNavController().navigate(R.id.action_buyerSearchProductByDiscountFragment_to_buyerProductFragment, bundle)
        }

        binding.buyerSearchProductRecyclerView.adapter = adapter

    }

    private fun setupObserver(){
        viewModel.searchProductResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    binding.buyerSearchProductProgressBar.gone()
                    showErrorToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.buyerSearchProductProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerSearchProductProgressBar.gone()
                    adapter.setData(it.data.data)
                }
            }
        }
    }


}