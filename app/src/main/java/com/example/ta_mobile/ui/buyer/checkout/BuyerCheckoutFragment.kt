package com.example.ta_mobile.ui.buyer.checkout

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerCheckoutBinding
import com.example.ta_mobile.ui.buyer.cart.BuyerCartAdapter
import com.example.ta_mobile.ui.buyer.checkout.voucher.BuyerCheckoutVoucherListDialogFragment
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class BuyerCheckoutFragment : Fragment() {

    private var _binding : FragmentBuyerCheckoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BuyerCheckoutViewModel by activityViewModel()


    private lateinit var adapter: BuyerCheckoutAdapter

    private var price:Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerCheckoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buyerCheckoutToolbar.setTitle("Checkout")
        binding.buyerCheckoutToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.calculateTotal()

        setupAdapter()
        setupObserver()
        setupButton()

    }

    private fun setupButton(){

        binding.buyerCheckoutPaymentDeliveryCard.root.setOnClickListener {
            val botSheet = PaymentAndDeliveryDialogFragment()
            botSheet.show(requireActivity().supportFragmentManager, "tag")
        }


        binding.buyerCheckoutVoucherCard.root.setOnClickListener {
            val botSheet = BuyerCheckoutVoucherListDialogFragment()
            botSheet.show(requireActivity().supportFragmentManager, "tag")
        }

        binding.buyerCheckoutOrderBtn.setOnClickListener {
            viewModel.calculateTotal()
            viewModel.addOrder()
        }
    }

    private fun setupAdapter(){
        binding.buyerCheckoutProductRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerCheckoutAdapter()

        binding.buyerCheckoutProductRv.adapter = adapter

    }

    private fun setupObserver(){
        viewModel.getProductCart().observe(viewLifecycleOwner){
            price = 0
            adapter.setData(it)
            it.forEach {
                price = (price + (it.price * it.amountPurchase)).toInt()
            }
            binding.buyerCheckoutPriceLayout.paymentDetailCardTotalSubtotalPriceTv.text = CurrencyHelper.convertToRupiah(price)
            viewModel.setSubtotal(price)
            viewModel.calculateTotal()
        }

        viewModel.deliveryMethod.observe(viewLifecycleOwner){
            binding.buyerCheckoutPaymentDeliveryCard.deliveryMethodTv.text = it
            if(it == "pickup"){
                binding.buyerCheckoutPriceLayout.paymentDetailCardShippingPriceAmountTv.text = CurrencyHelper.convertToRupiah(0)
            }else{
                binding.buyerCheckoutPriceLayout.paymentDetailCardShippingPriceAmountTv.text = CurrencyHelper.convertToRupiah(3000)
                viewModel.setShippingFee(3000)
            }
            viewModel.calculateTotal()
        }

        viewModel.paymentMethhod.observe(viewLifecycleOwner){
            binding.buyerCheckoutPaymentDeliveryCard.paymentMethodTv.text = it
            viewModel.calculateTotal()
        }

        viewModel.selectedVoucher.observe(viewLifecycleOwner){
            if(it == null){
                binding.buyerCheckoutVoucherCard.voucherCartTitle.text = "Select your voucher"
                binding.buyerCheckoutPriceLayout.paymentDetailCardDiscountPriceTv.text = CurrencyHelper.convertToRupiah(0)
            }else{
                binding.buyerCheckoutVoucherCard.voucherCartTitle.text = it?.title
                binding.buyerCheckoutPriceLayout.paymentDetailCardDiscountPriceTv.text = CurrencyHelper.convertToRupiah(it.amount)
            }
            viewModel.calculateTotal()
        }

        viewModel.totalPrice.observe(viewLifecycleOwner){
            binding.buyerCheckoutPriceLayout.paymentDetailCardTotalPriceTv.text = CurrencyHelper.convertToRupiah(it)
        }

        viewModel.orderFee.observe(viewLifecycleOwner){
            binding.buyerCheckoutPriceLayout.paymentDetailCardOrderPriceTv.text = CurrencyHelper.convertToRupiah(it)
        }

        viewModel.addOrderResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    binding.buyerCheckoutPB.gone()
                    showToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.buyerCheckoutPB.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerCheckoutPB.gone()
                    viewModel.clearData()
                    showToast("Success Order")
                    findNavController().navigate(R.id.action_buyerCheckoutFragment_to_buyerHomeFragment)
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}