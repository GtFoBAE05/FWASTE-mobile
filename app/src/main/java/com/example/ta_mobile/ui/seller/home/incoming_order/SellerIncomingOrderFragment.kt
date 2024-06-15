package com.example.ta_mobile.ui.seller.home.incoming_order

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.order.OrderDetailResponseData
import com.example.ta_mobile.databinding.FragmentSellerIncomingOrderBinding
import com.example.ta_mobile.ui.seller.home.SellerHomeViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showSuccessToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject


class SellerIncomingOrderFragment : Fragment() {

    private var _binding : FragmentSellerIncomingOrderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SellerHomeViewModel by inject()

    private lateinit var transactionId : String
    private lateinit var orderId : String

    private lateinit var adapter: SellerIncomingOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionId = requireArguments().getString("transactionId").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerIncomingOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sellerIncomingORderToolbar.title = "Incoming Order"
        binding.sellerIncomingORderToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        setupObserver()
        viewModel.getOrderDetail(transactionId)
        setupAdapter()
        setupButton()
    }

    private fun setupButton(){
        binding.sellerIncomingOrderAcceptBtn.setOnClickListener {
            viewModel.updateOrderStatus(transactionId)
        }

        binding.sellerIncomingOrderRejectBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Reject Order")
                .setMessage("Are you sure want to reject this order?")
                .setPositiveButton("Yes"){dialog, which ->
                    viewModel.rejectOrderStatus(orderId)
                }
                .setNegativeButton("No"){dialog, which ->
                    dialog.dismiss()
                }
                .create().show()
        }
    }

    private fun setupView(data : OrderDetailResponseData){
        binding.sellerIncomingOrderBuyerInfoCard.buyerInformationCardName.text = data.userName
        binding.sellerIncomingOrderBuyerInfoCard.buyerInformationCardPhoneNumber.text = data.userPhoneNumber
        binding.sellerIncomingOrderBuyerInfoCard.buyerInformationCardAddress.text = data.userAddress

        binding.sellerIncomingOrderBuyerInfoCard.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("lat", data.userLocation.split(",")[0])
            bundle.putString("lon", data.userLocation.split(",")[1])

            findNavController().navigate(R.id.action_sellerIncomingOrderFragment_to_sellerOrderMapsFragment, bundle )
        }
    }

    private fun setupAdapter(){
        binding.sellerIncomingOrderRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SellerIncomingOrderAdapter()
        binding.sellerIncomingOrderRv.adapter = adapter
    }

    private fun setupObserver(){
        viewModel.orderDetailData.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                    binding.sellerIncomingOrderProgressBar.gone()
                }
                NetworkResult.Loading -> {
                    binding.sellerIncomingOrderContentLayout.gone()
                    binding.sellerIncomingOrderProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.sellerIncomingOrderContentLayout.visible()
                    binding.sellerIncomingOrderProgressBar.gone()
                    setupView(it.data.data)
                    orderId = it.data.data.order.id
                    adapter.setData(it.data.data.order.product)
                }
            }
        }


        viewModel.updateOrderStatusResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                    binding.sellerIncomingOrderProgressBar.gone()
                    binding.sellerIncomingOrderContentLayout.visible()
                }
                NetworkResult.Loading -> {
                    binding.sellerIncomingOrderContentLayout.gone()
                    binding.sellerIncomingOrderProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.sellerIncomingOrderProgressBar.gone()
                    showSuccessToast("Success accept order")
                    findNavController().navigate(R.id.action_sellerIncomingOrderFragment_to_sellerHomeFragment)

                }
            }
        }

        viewModel.rejectOrderStatusResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                    binding.sellerIncomingOrderContentLayout.visible()
                    binding.sellerIncomingOrderProgressBar.gone()
                }
                NetworkResult.Loading -> {
                    binding.sellerIncomingOrderContentLayout.gone()
                    binding.sellerIncomingOrderProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.sellerIncomingOrderProgressBar.gone()
                    showSuccessToast("Success reject order")
                    findNavController().navigate(R.id.action_sellerIncomingOrderFragment_to_sellerHomeFragment)

                }
            }
        }
    }


}