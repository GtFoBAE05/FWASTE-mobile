package com.example.ta_mobile.ui.seller.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentSellerHomeBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject

class SellerHomeFragment : Fragment() {

   private var _binding : FragmentSellerHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SellerHomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sellerHomeToolbar.title = "Home"

        viewModel.getOrderStatus()
        setupView()
        setupObserver()
        setupButton()

    }

    private fun setupButton(){
        binding.btnSendNotificationToBuyer.setOnClickListener {
            findNavController().navigate(R.id.action_sellerHomeFragment_to_sellerSendNotificationFragment)
        }
    }

    private fun setupView(){
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.food_waste_ads_01, ))
        imageList.add(SlideModel(R.drawable.express_delivery_ads_02, ))
        binding.sellerHomeImageSlider.setImageList(imageList, ScaleTypes.FIT)

        binding.sellerHomeImageSlider.setOnClickListener {
        }


    }

    private fun setupObserver(){
        viewModel.orderStatusResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    binding.sellerHomePB.gone()
                    showErrorToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.sellerHomell.gone()
                    binding.sellerHomePB.visible()
                }
                is NetworkResult.Success -> {
                    binding.sellerHomell.visible()
                    binding.sellerHomePB.gone()

                    if(it.data.data.isNotEmpty()){
//                        binding.btnIncomingOrderProduct.visible()
//                        val bundle = Bundle()
//                        bundle.putString("transactionId", it.data.data.first().id)

                        binding.btnIncomingOrderProduct.setOnClickListener {
//                            findNavController().navigate(R.id.action_sellerHomeFragment_to_sellerIncomingOrderFragment, bundle)
                            findNavController().navigate(R.id.action_sellerHomeFragment_to_sellerOrderFragment)
                        }

                    }else{
                        binding.btnIncomingOrderProduct.gone()
                    }

                }
            }
        }

        viewModel.getUserName().observe(viewLifecycleOwner){
            binding.sellerHomeUserNameTv.text = "Welcome, ${it}"
        }
    }

}