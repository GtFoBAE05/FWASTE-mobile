package com.example.ta_mobile.ui.buyer.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.activityViewModels
import com.example.ta_mobile.databinding.FragmentPaymentAndDeliveryDialogListDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PaymentAndDeliveryDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentPaymentAndDeliveryDialogListDialogBinding? = null

    private val binding get() = _binding!!
    private val viewModel: BuyerCheckoutViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding =
            FragmentPaymentAndDeliveryDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupButton()
        setupObserver()
    }

    private fun setupButton(){


        binding.paymentDeliveryDialogConfirmBtn.setOnClickListener {
            val selectedPaymentId = binding.paymentDeliveryDialogPaymentGroup.checkedRadioButtonId
            val selectedDeliveryId = binding.paymentDeliveryDialogDeliveryGroup.checkedRadioButtonId

            val selectedPaymentMethod = view?.findViewById<RadioButton>(selectedPaymentId)?.text.toString()
            val selectedDeliveryMethod = view?.findViewById<RadioButton>(selectedDeliveryId)?.text.toString()

            viewModel.updatePaymentDelivery(selectedPaymentMethod, selectedDeliveryMethod)

            dismiss()
        }
    }

    private fun setupObserver(){
        viewModel.paymentMethhod.observe(viewLifecycleOwner){
            val radioButton = when (it) {
                "cash" -> binding.paymentDeliveryDialogCashRadio
                "e-wallet" -> binding.paymentDeliveryDialogEWalletRadio
                else -> null
            }
            radioButton?.isChecked = true
        }

        viewModel.deliveryMethod.observe(viewLifecycleOwner){
            val deliveryButton = when (it) {
                "pickup" -> binding.paymentDeliveryDialogCOD
                "delivery" -> binding.paymentDeliveryDialogCourierRadio
                else -> null
            }
            deliveryButton?.isChecked = true
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}