package com.example.ta_mobile.ui.seller.product.add

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
import androidx.navigation.fragment.findNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentSellerAddProductBinding
import com.example.ta_mobile.ui.seller.product.SellerProductViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showSuccessToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.createCustomTempFile
import com.example.ta_mobile.utils.helper.reduceFileImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class SellerAddProductFragment : Fragment() {

    private var _binding: FragmentSellerAddProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SellerProductViewModel by inject()

    private var currentImageUri: Uri? = null
    private var getFile: File? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sellerAddProductToolbar.title = "Add Product"
        binding.sellerAddProductToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        setupButton()
        setupObserver()
    }

    private fun setupButton() {
        binding.sellerAddProductAddImagebutton.setOnClickListener {
            startGallery()


        }

        binding.sellerAddProductAddBtn.setOnClickListener {
            checkAddProductForm()
        }
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
            binding.sellerAddProductimageView.visible()
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.sellerAddProductimageView.setImageURI(it)
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
        val name = binding.etsellerAddProductName.text.toString()
        val category = binding.etsellerAddProductCategory.text.toString().trim()
        val description = binding.etsellerAddProductDescription.text.toString().trim()
        val price = binding.etsellerAddProductPrice.text.toString().trim()
        val stock = binding.etsellerAddProductStock.text.toString().trim()
        val rack = binding.etsellerAddProductRackPosition.text.toString().trim()


        if (name.isEmpty()) {
            isError = true
            binding.etsellerAddProductName.error = getString(R.string.form_empty_message)
        }

        if (category.isEmpty()) {
            isError = true
            binding.etsellerAddProductCategory.error = getString(R.string.form_empty_message)
        }

        if (description.isEmpty()) {
            isError = true
            binding.etsellerAddProductDescription.error = getString(R.string.form_empty_message)
        }

        if (price.isEmpty()) {
            isError = true
            binding.etsellerAddProductPrice.error = getString(R.string.form_empty_message)
        }

        if (stock.isEmpty()) {
            isError = true
            binding.etsellerAddProductStock.error = getString(R.string.form_empty_message)
        }

        if (rack.isEmpty()) {
            isError = true
            binding.etsellerAddProductRackPosition.error = getString(R.string.form_empty_message)
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

            viewModel.addProduct(
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

    private fun setupObserver() {
        viewModel.addProductResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                    binding.sellerAddProductprogressBar.gone()
                }

                NetworkResult.Loading -> {
                    binding.sellerAddProductprogressBar.visible()
                }

                is NetworkResult.Success -> {
                    showSuccessToast("Product added successfully")
                    binding.sellerAddProductprogressBar.gone()
                    findNavController().popBackStack()
                }
            }
        }
    }


}