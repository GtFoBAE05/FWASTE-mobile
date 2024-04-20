package com.example.ta_mobile.ui.buyer.order.detail

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.buyer.order.BuyerOrderDetailResponse
import com.example.ta_mobile.data.source.remote.response.buyer.order.BuyerOrderDetailResponseData
import com.example.ta_mobile.data.source.remote.response.buyer.order.BuyerOrderStatusResponseData
import com.example.ta_mobile.databinding.FragmentBuyerOrderDetailBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.CurrencyHelper
import org.koin.android.ext.android.inject

class BuyerOrderDetailFragment : Fragment() {

    private var _binding: FragmentBuyerOrderDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BuyerOrderDetailViewModel by inject()

    private lateinit var transactionId : String

    private lateinit var adapter: BuyerOrderDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionId = requireArguments().getString("transactionId").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerOrderDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buyerCheckoutToolbar.setTitle("Order Detail")
        binding.buyerCheckoutToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        setupAdapter()
        viewModel.getOrderDetail(transactionId)
        setupObserver()
        setupButton()
    }

    private fun setupButton(){
        binding.buyerOrderDetailUpdateBtn.setOnClickListener {
            viewModel.updateOrderStatus(transactionId)
        }
    }

    private fun setupView(data : BuyerOrderDetailResponseData){
        if(data.shippingMethod.equals("pickup")){
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusOnTheWay.text = "ready to pickup"
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusArrived.text = "finished"
        }else{
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusOnTheWay.text = "on the way"
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusArrived.text = "arrived"
        }

        if(data.order.orderStatus.equals("waiting confirmation")){
            binding.buyerOrderDetailDeliveryMethodCard.deliveryCardDetailSubtitle.visible()
        }else{
            binding.buyerOrderDetailDeliveryMethodCard.deliveryCardDetailSubtitle.gone()
        }

        if(data.order.orderStatus.equals("confirmed")){
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusConfirmedTv.setTextColor(getResources().getColor(R.color.success_main))
        }

        if (data.order.orderStatus.equals("on the way") || data.order.orderStatus.equals("ready to pickup")){
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusConfirmedTv.setTextColor(getResources().getColor(R.color.success_main))
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusDivider1.setBackgroundColor(getResources().getColor(R.color.success_main))
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusOnTheWay.setTextColor(getResources().getColor(R.color.success_main))

            binding.buyerOrderDetailUpdateBtn.visible()
        }

        if (data.order.orderStatus.equals("arrived") || data.order.orderStatus.equals("finished")){
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusConfirmedTv.setTextColor(getResources().getColor(R.color.success_main))
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusDivider1.setBackgroundColor(getResources().getColor(R.color.success_main))
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusOnTheWay.setTextColor(getResources().getColor(R.color.success_main))
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusDivider2.setBackgroundColor(getResources().getColor(R.color.success_main))
            binding.buyerOrderDetailDeliveryMethodCard.buyerOrderStatusArrived.setTextColor(getResources().getColor(R.color.success_main))

        }

        binding.buyerOrderDetailPaymentDeliveryCard.imageView.gone()
        binding.buyerOrderDetailPaymentDeliveryCard.paymentMethodTv.text = data.paymentMethod
        binding.buyerOrderDetailPaymentDeliveryCard.deliveryMethodTv.text = data.shippingMethod

        binding.buyerOrderDetailPriceLayout.paymentDetailCardTotalSubtotalPriceTv.text = CurrencyHelper.convertToRupiah(data.order.subtotal)
        binding.buyerOrderDetailPriceLayout.paymentDetailCardShippingPriceAmountTv.text = CurrencyHelper.convertToRupiah(data.order.deliveryFee)
        binding.buyerOrderDetailPriceLayout.paymentDetailCardOrderPriceTv.text = CurrencyHelper.convertToRupiah(data.order.serviceFee)
        binding.buyerOrderDetailPriceLayout.paymentDetailCardDiscountPriceTv.text = CurrencyHelper.convertToRupiah(data.order.discountAmount)
        binding.buyerOrderDetailPriceLayout.paymentDetailCardTotalPriceTv.text = CurrencyHelper.convertToRupiah(data.totalAmount)



    }

    private fun setupAdapter(){
        binding.buyerOrderDetailProductRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerOrderDetailAdapter()
        binding.buyerOrderDetailProductRv.adapter = adapter
    }

    private fun setupObserver(){
        viewModel.orderDetailData.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error ->{
                    binding.buyerOrderDetailProgressBar.gone()
                }
                NetworkResult.Loading -> {
                    binding.buyerOrderDetailProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerOrderDetailProgressBar.gone()
                    adapter.setData(it.data.data.order.product)
                    setupView(it.data.data)
                }
            }
        }

        viewModel.updateOrderStatusResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error ->{
                    binding.buyerOrderDetailProgressBar.gone()
                }
                NetworkResult.Loading -> {
                    binding.buyerOrderDetailProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerOrderDetailProgressBar.gone()
                    findNavController().navigate(R.id.action_buyerOrderDetailFragment_to_buyerHomeFragment)
                    showToast("Success Update Order")
                }
            }
        }
    }


}