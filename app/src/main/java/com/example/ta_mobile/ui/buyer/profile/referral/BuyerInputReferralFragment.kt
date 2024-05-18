package com.example.ta_mobile.ui.buyer.profile.referral

import android.content.DialogInterface
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerInputReferralListDialogBinding
import com.example.ta_mobile.ui.buyer.profile.BuyerProfileViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showSuccessToast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class BuyerInputReferralFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBuyerInputReferralListDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BuyerProfileViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBuyerInputReferralListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupButton()
    }

    private fun checkField(){
        var isError = false
        val referralEt = binding.etRegisterReferral.text.toString().trim()

        if (referralEt.isEmpty()) {
            isError = true
            binding.etRegisterReferral.error = getString(R.string.form_empty_message)
        }

        if (!isError) {
            viewModel.inputReferral(referralEt)
        }
    }

    private fun setupButton(){
        binding.btnContinue.setOnClickListener {
            checkField()
        }
    }

    private fun setupObserver(){
        viewModel.inputReferralResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                }
                NetworkResult.Loading -> {}
                is NetworkResult.Success -> {
                    showSuccessToast("Success Input Referral")
                    findNavController().navigate(R.id.action_buyerProfileFragment_to_buyerHomeFragment)
                    dismiss()
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.viewModelStore?.clear()
    }



}