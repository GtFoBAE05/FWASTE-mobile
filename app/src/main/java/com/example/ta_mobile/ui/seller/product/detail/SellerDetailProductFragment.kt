package com.example.ta_mobile.ui.seller.product.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMySingleProductData
import com.example.ta_mobile.databinding.FragmentSellerDetailProductBinding
import com.example.ta_mobile.ui.seller.product.SellerProductViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.CurrencyHelper
import org.koin.android.ext.android.inject


class SellerDetailProductFragment : Fragment() {

    private var _binding: FragmentSellerDetailProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel : SellerProductViewModel by inject()

    private lateinit var productId:String
    var  productVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = requireArguments().getString("productId").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sellerDetailProductToolbar.title = "Detail Product"

        binding.sellerDetailProductToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getSingleProduct(productId)

        setupButton()
        setupObserver()

    }

    private fun setupView(data : SellerGetMySingleProductData){
        Glide.with(binding.root).load(data.products.imageUrl).into(binding.productDetailImageView)

        binding.productDetailProductName.text = data.products.name
        binding.productDetailCategoryTv.text = "Category : ${data.products.category}"
        binding.productDetailDescriptionTv.text = data.products.description
        binding.productDetailPriceTv.text = CurrencyHelper.convertToRupiah(data.products.price)
        binding.productDetailStockCount.text = "Stock : ${data.products.stockCount}"
        binding.productDetailRackPosition.text = "Rack Position : ${data.products.rackPosition}"

    }

    private fun setupButton(){
        binding.productDetailEditBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("productId", productId)
            findNavController().navigate(R.id.action_sellerDetailProductFragment_to_sellerEditProductFragment, bundle)
        }

        binding.productDetailDeleteProduct.setOnClickListener {
            viewModel.deleteProduct(productId)
        }

        binding.productDetailUpdateVisibilitybtn.setOnClickListener {
            viewModel.setMyProductVisibility(productId, !productVisible)
            showToast("Product Visibility Updated")
        }
    }

    private fun setupObserver(){
        viewModel.mySingleProductData.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Success -> {
                    binding.sellerDetailProgressBar.gone()
                    setupView(it.data.data)

                    productVisible = it.data.data.products.isVisible


                    if (productVisible) {
                        binding.productDetailUpdateVisibilitybtn.text = "Set as Hidden"
                    } else {
                        binding.productDetailUpdateVisibilitybtn.text = "Set as Visible"
                    }



                }

                is NetworkResult.Error -> {
                    binding.sellerDetailProgressBar.gone()
                    showToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.sellerDetailProgressBar.visible()
                }
            }
        }

        viewModel.setProductVisibility.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Success -> {
                    binding.sellerDetailProgressBar.gone()
                    viewModel.getSingleProduct(productId)


                }

                is NetworkResult.Error -> {
                    binding.sellerDetailProgressBar.gone()
                    showToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.sellerDetailProgressBar.visible()
                }
            }
        }

        viewModel.deleteProductResponse.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Success -> {
                    binding.sellerDetailProgressBar.gone()
                    findNavController().popBackStack()
                    showToast("Product Deleted")


                }

                is NetworkResult.Error -> {
                    binding.sellerDetailProgressBar.gone()
                    showToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.sellerDetailProgressBar.visible()
                }
            }
        }
    }




}