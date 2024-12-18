package com.example.ta_mobile.ui.seller.product.detail

import android.app.AlertDialog
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMySingleProductData
import com.example.ta_mobile.databinding.FragmentSellerDetailProductBinding
import com.example.ta_mobile.ui.seller.product.SellerProductViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showSuccessToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DateTimeHelper
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
    ): View {
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
        binding.productDetailOriginalPriceTv.text = CurrencyHelper.convertToRupiah(data.products.originalPrice)
        binding.productDetailStockCount.text = "Stock : ${data.products.stockCount}"
        binding.productDetailRackPosition.text = "Rack Position : ${data.products.rackPosition}"
        binding.productDetailProductionDateTv.text =
            "Production Date: ${DateTimeHelper.convertDate(data.products.productionDate)}"
        binding.productDetailExpireDateTv.text =
            "Expire Date: ${DateTimeHelper.convertDate(data.products.expireDate)}"

        binding.productDetailOriginalPriceTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun setupButton(){
        binding.productDetailEditBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("productId", productId)
            findNavController().navigate(R.id.action_sellerDetailProductFragment_to_sellerEditProductFragment, bundle)
        }

        binding.productDetailDeleteProduct.setOnClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Delete Product")
                .setMessage("Are you sure want to delete this product?")
                .setPositiveButton("Yes") { dialog, which ->
                    dialog.dismiss()
                    viewModel.deleteProduct(productId)
                }
                .setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }

            dialog.show()
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
                    binding.sellerDetailProductScrollView.visible()
                    binding.sellerDetailProgressBar.gone()
                    setupView(it.data.data)

                    productVisible = it.data.data.products.isVisible


                    if (productVisible) {
                        binding.productDetailUpdateVisibilitybtn.text = "Hide this product"
                    } else {
                        binding.productDetailUpdateVisibilitybtn.text = "Show this product"
                    }



                }

                is NetworkResult.Error -> {
                    binding.sellerDetailProgressBar.gone()
                    showToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.sellerDetailProductScrollView.gone()
                    binding.sellerDetailProgressBar.visible()
                }
            }
        }

        viewModel.setProductVisibility.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Success -> {
                    binding.sellerDetailProductScrollView.visible()
                    binding.sellerDetailProgressBar.gone()
                    viewModel.getSingleProduct(productId)


                }

                is NetworkResult.Error -> {
                    binding.sellerDetailProgressBar.gone()
                    showErrorToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.sellerDetailProductScrollView.gone()
                    binding.sellerDetailProgressBar.visible()
                }
            }
        }

        viewModel.deleteProductResponse.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Success -> {
                    binding.sellerDetailProductScrollView.visible()
                    binding.sellerDetailProgressBar.gone()
                    findNavController().popBackStack()
                    showSuccessToast("Product Deleted")


                }

                is NetworkResult.Error -> {
                    binding.sellerDetailProgressBar.gone()
                    showErrorToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.sellerDetailProductScrollView.gone()
                    binding.sellerDetailProgressBar.visible()
                }
            }
        }
    }




}