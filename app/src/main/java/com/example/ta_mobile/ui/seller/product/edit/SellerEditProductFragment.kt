package com.example.ta_mobile.ui.seller.product.edit

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.seller.product.SellerGetMySingleProductData
import com.example.ta_mobile.databinding.FragmentSellerEditProductBinding
import com.example.ta_mobile.ui.seller.product.SellerProductViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showSuccessToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.createCustomTempFile
import com.example.ta_mobile.utils.helper.downloadImageAsTempFile
import com.example.ta_mobile.utils.helper.reduceFileImage
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class SellerEditProductFragment : Fragment() {

    private var _binding: FragmentSellerEditProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel : SellerProductViewModel by inject()
    private lateinit var productId:String

    private var currentImageUri : Uri? = null
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        productId = requireArguments().getString("productId").toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerEditProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sellerEditProductToolbar.title = "Edit Product"
        binding.sellerEditProductToolbar.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getSingleProduct(productId)
        setupObserver()
        setupButton()


    }

    private fun setupButton(){
        binding.sellerEditProductEditImagebutton.setOnClickListener {
            startGallery()
        }

        binding.sellerEditProductEditBtn.setOnClickListener {
            checkAddProductForm()
        }

    }

    private fun setupView(data : SellerGetMySingleProductData){
        lifecycleScope.launch {
            getFile = downloadImageAsTempFile(requireContext(), data.products.imageUrl)
        }

        Glide.with(binding.root).load(data.products.imageUrl).into(binding.sellerEditProductimageView)
        binding.etsellerEditProductName.setText(data.products.name)
        binding.etsellerEditProductCategory.setText(data.products.category)
        binding.etsellerEditProductDescription.setText(data.products.description)
        binding.etsellerEditProductPrice.setText(data.products.price.toString())
        binding.etsellerEditProductStock.setText(data.products.stockCount.toString())
        binding.etsellerEditProductRackPosition.setText(data.products.rackPosition.toString())

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            val myFile = uriToFile(uri, requireActivity())
            getFile = myFile
            binding.sellerEditProductimageView.visible()
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.sellerEditProductimageView.setImageURI(it)
        }
    }

    fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(
            buffer,
            0,
            length
        )
        outputStream.close()
        inputStream.close()
        return myFile
    }

    private fun checkAddProductForm() {
        var isError = false
        val name = binding.etsellerEditProductName.text.toString()
        val category = binding.etsellerEditProductCategory.text.toString().trim()
        val description = binding.etsellerEditProductDescription.text.toString().trim()
        val price = binding.etsellerEditProductPrice.text.toString().trim()
        val stock = binding.etsellerEditProductStock.text.toString().trim()
        val rack = binding.etsellerEditProductRackPosition.text.toString().trim()


        if (name.isEmpty()) {
            isError = true
            binding.etsellerEditProductName.error = getString(R.string.form_empty_message)
        }

        if (category.isEmpty()) {
            isError = true
            binding.etsellerEditProductCategory.error = getString(R.string.form_empty_message)
        }

        if (description.isEmpty()) {
            isError = true
            binding.etsellerEditProductDescription.error = getString(R.string.form_empty_message)
        }

        if (price.isEmpty()) {
            isError = true
            binding.etsellerEditProductPrice.error = getString(R.string.form_empty_message)
        }

        if (stock.isEmpty()) {
            isError = true
            binding.etsellerEditProductStock.error = getString(R.string.form_empty_message)
        }

        if (rack.isEmpty()) {
            isError = true
            binding.etsellerEditProductRackPosition.error = getString(R.string.form_empty_message)
        }

        if (currentImageUri == null) {
            isError = true
            showToast("Please add image")
        }

        if (!isError) {
            val file = getFile as File
            val reducedImageSize = reduceFileImage(file)
            val image = reducedImageSize.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                image
            )

            viewModel.editProduct(
                productId,
                imagePart,
                name,
                category,
                description,
                price,
                stock,
                rack
            )
        }
    }


    private fun setupObserver(){
        viewModel.mySingleProductData.observe(viewLifecycleOwner){
            when(it){

                is NetworkResult.Error -> {
                    binding.sellerEditProductprogressBar.gone()
                    showErrorToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.sellerEditProductprogressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.sellerEditProductprogressBar.gone()
                    setupView(it.data.data)
                }
            }
        }

        viewModel.editProductResponse.observe(viewLifecycleOwner){
            when(it){

                is NetworkResult.Error -> {
                    binding.sellerEditProductprogressBar.gone()
                    showErrorToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.sellerEditProductprogressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.sellerEditProductprogressBar.gone()
                    showSuccessToast("Product edited successfully")
                    findNavController().popBackStack()
                }
            }
        }
    }

}