package com.example.ta_mobile.ui.seller.order.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.order.OrderDetailResponseData
import com.example.ta_mobile.databinding.FragmentSellerDetailOrderBinding
import com.example.ta_mobile.ui.buyer.order.detail.BuyerOrderDetailAdapter
import com.example.ta_mobile.ui.buyer.order.detail.BuyerOrderDetailViewModel
import com.example.ta_mobile.ui.seller.order.SellerOrderViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.CurrencyHelper
import org.koin.android.ext.android.inject


class SellerDetailOrderFragment : Fragment() {

    private var _binding: FragmentSellerDetailOrderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SellerOrderViewModel by inject()

    private lateinit var transactionId : String


    private lateinit var adapter: SellerOrderDetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionId = requireArguments().getString("transactionId").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerDetailOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sellerCheckoutToolbar.setTitle("Order Detail")
        binding.sellerCheckoutToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        setupAdapter()
        viewModel.getOrderDetail(transactionId)
        setupObserver()
        setupButton()
    }

    private fun setupButton(){
        binding.sellerOrderDetailUpdateBtn.setOnClickListener {
            viewModel.updateOrderStatus(transactionId)
        }


    }

    private fun setupView(data : OrderDetailResponseData){
        if(data.shippingMethod.equals("pickup")){
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusOnTheWay.text = "ready to pickup"
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusArrived.text = "finished"
        }else{
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusOnTheWay.text = "on the way"
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusArrived.text = "arrived"
        }

        if(data.order.orderStatus.equals("waiting confirmation")){
            binding.sellerOrderDetailDeliveryMethodCard.deliveryCardDetailSubtitle.visible()
        }else{
            binding.sellerOrderDetailDeliveryMethodCard.deliveryCardDetailSubtitle.gone()
        }

        if(data.order.orderStatus.equals("confirmed")){
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusConfirmedTv.setTextColor(getResources().getColor(R.color.success_main))

            binding.sellerOrderDetailUpdateBtn.visible()


        }

        if (data.order.orderStatus.equals("on the way") || data.order.orderStatus.equals("ready to pickup")){
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusConfirmedTv.setTextColor(getResources().getColor(R.color.success_main))
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusDivider1.setBackgroundColor(getResources().getColor(R.color.success_main))
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusOnTheWay.setTextColor(getResources().getColor(R.color.success_main))

        }

        if (data.order.orderStatus.equals("arrived") || data.order.orderStatus.equals("finished")){
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusConfirmedTv.setTextColor(getResources().getColor(R.color.success_main))
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusDivider1.setBackgroundColor(getResources().getColor(R.color.success_main))
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusOnTheWay.setTextColor(getResources().getColor(R.color.success_main))
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusDivider2.setBackgroundColor(getResources().getColor(R.color.success_main))
            binding.sellerOrderDetailDeliveryMethodCard.buyerOrderStatusArrived.setTextColor(getResources().getColor(R.color.success_main))

        }

        binding.sellerOrderDetailPaymentDeliveryCard.imageView.gone()
        binding.sellerOrderDetailPaymentDeliveryCard.paymentMethodTv.text = data.paymentMethod
        binding.sellerOrderDetailPaymentDeliveryCard.deliveryMethodTv.text = data.shippingMethod

        binding.sellerOrderDetailPriceLayout.paymentDetailCardTotalSubtotalPriceTv.text = CurrencyHelper.convertToRupiah(data.order.subtotal)
        binding.sellerOrderDetailPriceLayout.paymentDetailCardShippingPriceAmountTv.text = CurrencyHelper.convertToRupiah(data.order.deliveryFee)
        binding.sellerOrderDetailPriceLayout.paymentDetailCardOrderPriceTv.text = CurrencyHelper.convertToRupiah(data.order.serviceFee)
        binding.sellerOrderDetailPriceLayout.paymentDetailCardDiscountPriceTv.text = CurrencyHelper.convertToRupiah(data.order.discountAmount)
        binding.sellerOrderDetailPriceLayout.paymentDetailCardTotalPriceTv.text = CurrencyHelper.convertToRupiah(data.totalAmount)

        binding.sellerIncomingOrderBuyerInfoCard.buyerInformationCardName.text = data.userName
        binding.sellerIncomingOrderBuyerInfoCard.buyerInformationCardPhoneNumber.text = data.userPhoneNumber
        binding.sellerIncomingOrderBuyerInfoCard.buyerInformationCardAddress.text = data.userAddress

        binding.sellerIncomingOrderBuyerInfoCard.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("lat", data.userLocation.split(",")[0])
            bundle.putString("lon", data.userLocation.split(",")[1])

            findNavController().navigate(R.id.action_sellerDetailOrderFragment_to_sellerOrderMapsFragment, bundle )
        }

    }

    private fun setupAdapter(){
        binding.sellerOrderDetailProductRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SellerOrderDetailAdapter()
        binding.sellerOrderDetailProductRv.adapter = adapter
    }

    private fun setupObserver(){
        viewModel.orderDetailData.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error ->{
                    binding.sellerOrderPB.gone()
                }
                NetworkResult.Loading -> {
                    binding.sellerOrderPB.visible()
                }
                is NetworkResult.Success -> {
                    binding.sellerOrderPB.gone()
                    adapter.setData(it.data.data.order.product)
                    setupView(it.data.data)
                }
            }
        }

        viewModel.updateOrderStatusResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error ->{
                    binding.sellerOrderPB.gone()
                }
                NetworkResult.Loading -> {
                    binding.sellerOrderPB.visible()
                }
                is NetworkResult.Success -> {
                    binding.sellerOrderPB.gone()
                    findNavController().popBackStack()
                    showToast("Success Update Order")
                }
            }
        }
    }


}