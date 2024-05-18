package com.example.ta_mobile.ui.buyer.checkout.voucher

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.databinding.FragmentBuyerCheckoutVoucherListDialogListDialogBinding
import com.example.ta_mobile.ui.buyer.checkout.BuyerCheckoutViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class BuyerCheckoutVoucherListDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBuyerCheckoutVoucherListDialogListDialogBinding? = null

    private val binding get() = _binding!!


    private val viewModel: BuyerCheckoutViewModel by activityViewModel()

    private lateinit var adapter: BuyerCheckoutVoucherListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

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
            adapter.setItemId("")
            dismiss()
        }
    }

    private fun setupAdapter(){
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerCheckoutVoucherListAdapter({
            viewModel.setSelectedVoucher(it)
            Log.e("TAG", "setupAdapter: ", )
            dismiss()
        },)

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
                        Log.e("TAG", "setupObserver: " + it.data.data, )

                        adapter.setData(it.data.data)
                    }
                }
            }
        }

        viewModel.selectedVoucher.observe(viewLifecycleOwner){
            if(it?.id != null){
                binding.BuyerCheckoutButtonCancelVoucher.visible()
                adapter.setItemId(it.id)
            }else{
                binding.BuyerCheckoutButtonCancelVoucher.gone()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}