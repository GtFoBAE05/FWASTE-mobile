package com.example.ta_mobile.ui.buyer.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerSearchStoreBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject

class BuyerSearchStoreFragment : Fragment() {

    private lateinit var _binding: FragmentBuyerSearchStoreBinding
    private val binding get() = _binding

    private val viewModel: BuyerSearchStoreViewModel by inject()

    private lateinit var keyword:String

    private lateinit var adapter: BuyerSearchStoreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = requireArguments().getString("keyword").toString()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerSearchStoreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getStoreNearBuyer(keyword)

        setupButton()
        setupSearchbar()
        setupAdapter()
        setupObserver()

    }

    private fun setupButton(){
        binding.buyerSearchBarBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSearchbar(){
        binding.etSearch.setText(keyword)

        binding.buyerSearchllSearchBar.setOnClickListener {
            binding.etSearch.requestFocus()
            val imm =
                context?.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imm.showSoftInput(
                binding.etSearch, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
            )
        }

        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {

                viewModel.getStoreNearBuyer(textView.text.trim().toString())

                //hide keyboard
                val imm =
                    context?.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                true
            } else {
                false
            }
        }
    }

    private fun setupAdapter(){
        binding.buyerSearchRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerSearchStoreAdapter {
            val bundle = Bundle()
            bundle.putString("storeId", it.id)
            findNavController().navigate(R.id.action_buyerSearchStoreFragment_to_buyerStoreDetailFragment, bundle)
        }

        binding.buyerSearchRv.adapter = adapter

    }

    private fun setupObserver(){
        viewModel.searchStoreResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    binding.buyerSearchProgressBar.gone()
                    showToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.buyerSearchProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerSearchProgressBar.gone()
                    adapter.setData(it.data.data)
                }
            }
        }
    }

}