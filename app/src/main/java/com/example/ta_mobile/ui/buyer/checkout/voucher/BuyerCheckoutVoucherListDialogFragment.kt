package com.example.ta_mobile.ui.buyer.checkout.voucher

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerCheckoutVoucherListDialogListDialogBinding
import com.example.ta_mobile.ui.buyer.checkout.BuyerCheckoutAdapter
import com.example.ta_mobile.ui.buyer.checkout.BuyerCheckoutViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.showToast
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class BuyerCheckoutVoucherListDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBuyerCheckoutVoucherListDialogListDialogBinding? = null

    private val binding get() = _binding!!


    private val viewModel: BuyerCheckoutViewModel by activityViewModel()

    private lateinit var adapter: BuyerCheckoutVoucherListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBuyerCheckoutVoucherListDialogListDialogBinding.inflate(
            inflater, container, false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserOwnedVoucher()
        setupAdapter()
        setupObserver()
        setupButton()
    }

    private fun setupButton(){
        binding.BuyerCheckoutButtonCancelVoucher.setOnClickListener {
            viewModel.clearSelectedVoucher()
            dismiss()
        }
    }

    private fun setupAdapter(){
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerCheckoutVoucherListAdapter{
            viewModel.setSelectedVoucher(it)
            Log.e("TAG", "setupAdapter: ", )
            dismiss()
        }

        binding.list.adapter = adapter

    }

    private fun setupObserver(){
        viewModel.userVoucherList.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                }
                NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    if(it.data.data.isEmpty()){
                        showToast("No Voucher")
                        dismiss()
                    }else{
                        adapter.setData(it.data.data)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}