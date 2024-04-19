package com.example.ta_mobile.ui.buyer.product

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.buyer.product.ProductDetailDataProductDataResponse
import com.example.ta_mobile.data.source.remote.response.buyer.product.ProductDetailResponse
import com.example.ta_mobile.databinding.FragmentBuyerProductBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.CurrencyHelper
import org.koin.android.ext.android.inject

class BuyerProductFragment : Fragment() {

    private lateinit var _binding : FragmentBuyerProductBinding
    private val binding get() = _binding

    private val viewModel: BuyerProductViewModel by inject()

    private lateinit var productId : String
    private lateinit var data: ProductDetailDataProductDataResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = requireArguments().getString("productId").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buyerProductDetailToolbar.setTitle("Product Detail")
        viewModel.getProductDetail(productId)

        setupButton()
        setupObserver()

    }

    private fun setupButton(){

        binding.buyerProductDetailToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        var count = 1
        binding.buyerProductScreenCountTv.text = count.toString()

        binding.buyerProductDetailBotNavBarMinusBtn.isEnabled = count>1

        binding.buyerProductScreenBotNavBarAddToCartBtn.isEnabled =
            binding.buyerProductScreenCountTv.text.toString().toInt()>0

        binding.buyerProductDetailBotNavBarMinusBtn.setOnClickListener {
            count--
            binding.buyerProductScreenCountTv.text = count.toString()
            binding.buyerProductDetailBotNavBarMinusBtn.isEnabled = count>1
        }

        binding.buyerProductDetailScreenBotNavBarPlusBtn.setOnClickListener {
            count++
            binding.buyerProductScreenCountTv.text = count.toString()
            binding.buyerProductDetailBotNavBarMinusBtn.isEnabled = count>1
        }

        binding.buyerProductScreenBotNavBarAddToCartBtn.setOnClickListener {
            viewModel.checkIfStoreValid(data.storeId).observe(viewLifecycleOwner){
                if(it){
                    viewModel.insertCartProduct(data.id, data.storeId, data.storeName, data.imageUrl, data.name, data.price.toDouble(), count)
                    findNavController().popBackStack()
                    showToast("Success Add To Cart")
                }else{
                    showToast("All cart product has changed")
                    AlertDialog.Builder(requireContext())
                        .setTitle("Are you sure want to change cart product from this store?")
                        .setMessage("Cart will get cleared")
                        .setPositiveButton(
                            "Yes"
                        ) { dialogInterface, i ->
                            viewModel.clearCartProduct()
                            viewModel.insertCartProduct(
                                data.id,
                                data.storeId,
                                data.storeName,
                                data.imageUrl,
                                data.name,
                                data.price.toDouble(),
                                count
                            )
                            findNavController().popBackStack()
                            showToast("Success Add To Cart")
                        }
                        .setNegativeButton(
                            "No"
                        ){dialogInterface, i ->
                            dialogInterface.cancel()
                        }.show()
                }
            }
        }

    }

    private fun setupObserver(){
        viewModel.productDetailResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    showToast(it.error)
                    binding.buyerProductDetailProgressBar.gone()
                }
                NetworkResult.Loading -> {
                    binding.buyerProductDetailProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerProductDetailProgressBar.gone()

                    data = it.data.data.products

                    Glide.with(binding.root).load(it.data.data.products.imageUrl).into(binding.buyerProductDetailImageView)
                    binding.buyerProductDetailPriceTv.setText(CurrencyHelper.convertToRupiah(it.data.data.products.price))
                    binding.buyerProductScreenTitleTv.setText(it.data.data.products.name)
                    binding.buyerProductDetailDescTv.setText(it.data.data.products.description)

                    Glide.with(binding.root).load(it.data.data.storeImageUrl).into(binding.buyerProductDetailScreenStoreCard.storeNameWithLocImageView)
                    binding.buyerProductDetailScreenStoreCard.storeNameWithLocTitleTv.setText(it.data.data.storeName)
                    binding.buyerProductDetailScreenStoreCard.storeNameWithLocLocationTv.setText(it.data.data.storeAddress)
                }
            }
        }
    }


}