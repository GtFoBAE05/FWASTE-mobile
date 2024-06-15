package com.example.ta_mobile.ui.seller.product.edit

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
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
import com.example.ta_mobile.utils.helper.DateTimeHelper
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class SellerEditProductFragment : Fragment() {

    private var _binding: FragmentSellerEditProductBinding? = null
    private val binding get() = _binding!!

    private val viewModel : SellerProductViewModel by inject()
    private lateinit var productId:String


    private val calendar = Calendar.getInstance()

    private var prodYear:Int = Calendar.getInstance().get(Calendar.YEAR)
    private var prodMonth:Int = Calendar.getInstance().get(Calendar.MONTH)
    private var prodDay:Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    private var expYear:Int = Calendar.getInstance().get(Calendar.YEAR)
    private var expMonth:Int = Calendar.getInstance().get(Calendar.MONTH)
    private var expDay:Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

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
        setupEditText()


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
        binding.etsellerEditProductCategory.setSelection(resources.getStringArray(R.array.food_category_array).indexOf(data.products.category))
        binding.etsellerEditProductDescription.setText(data.products.description)
        binding.etsellerEditProductPrice.setText(data.products.price.toString())
        binding.etsellerEditProductOriginalPrice.setText(data.products.originalPrice.toString())
        binding.etsellerEditProductStock.setText(data.products.stockCount.toString())
        binding.etsellerEditProductRackPosition.setText(data.products.rackPosition.toString())
        binding.etsellerEditProductProductionDate.setText(DateTimeHelper.convertDate(data.products.productionDate))
        binding.etsellerEditProductExpireDate.setText(DateTimeHelper.convertDate(data.products.expireDate))

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

    private fun setupEditText() {

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.food_category_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.etsellerEditProductCategory.adapter = it
        }



        binding.etsellerEditProductProductionDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(), { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val cProdDate = Calendar.getInstance()
                    cProdDate.set(prodYear, prodMonth, prodDay)

                    val cExpDate = Calendar.getInstance()
                    cExpDate.set(expYear, expMonth, expDay)

                    if (cProdDate.after(cExpDate)) {
                        showToast("Production date must be before expire date")
                        binding.etsellerEditProductExpireDate.text.clear()

                        expYear = Calendar.getInstance().get(Calendar.YEAR)
                        expMonth = Calendar.getInstance().get(Calendar.MONTH)
                        expDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

                        return@DatePickerDialog
                    }

                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)
                    prodYear = year
                    prodMonth = monthOfYear
                    prodDay = dayOfMonth
                    binding.etsellerEditProductProductionDate.setText(formattedDate)


                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )


            datePickerDialog.show()
        }

        binding.etsellerEditProductExpireDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(), {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(selectedDate.time)

                    expYear = year
                    expMonth = monthOfYear
                    expYear = dayOfMonth

                    binding.etsellerEditProductExpireDate.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()


            datePickerDialog.show()
        }

    }

    private fun checkAddProductForm() {
        var isError = false
        val name = binding.etsellerEditProductName.text.toString()
        val category = binding.etsellerEditProductCategory.selectedItem.toString()
        val description = binding.etsellerEditProductDescription.text.toString().trim()
        val price = binding.etsellerEditProductPrice.text.toString().trim()
        val originalPrice = binding.etsellerEditProductOriginalPrice.text.toString().trim()
        val stock = binding.etsellerEditProductStock.text.toString().trim()
        val rack = binding.etsellerEditProductRackPosition.text.toString().trim()
        val prodDate = binding.etsellerEditProductProductionDate.text.toString().trim()
        val expDate = binding.etsellerEditProductExpireDate.text.toString().trim()

        if (name.isEmpty()) {
            isError = true
            binding.etsellerEditProductName.error = getString(R.string.form_empty_message)
        }

        if (category.isEmpty()) {
            isError = true
            val spinner = binding.etsellerEditProductCategory.selectedView as TextView
            spinner.error = getString(R.string.form_empty_message)
        }

        if (description.isEmpty()) {
            isError = true
            binding.etsellerEditProductDescription.error = getString(R.string.form_empty_message)
        }

        if (price.isEmpty()) {
            isError = true
            binding.etsellerEditProductPrice.error = getString(R.string.form_empty_message)
        }

        if (originalPrice.isEmpty()) {
            isError = true
            binding.etsellerEditProductOriginalPrice.error = getString(R.string.form_empty_message)
        }

        if (stock.isEmpty()) {
            isError = true
            binding.etsellerEditProductStock.error = getString(R.string.form_empty_message)
        }

        if (rack.isEmpty()) {
            isError = true
            binding.etsellerEditProductRackPosition.error = getString(R.string.form_empty_message)
        }

        if (originalPrice.toInt()<price.toInt()){
            isError = true
            showToast("Original price must be greater than price")
        }

        if (prodDate.isEmpty()) {
            isError = true
            binding.etsellerEditProductProductionDate.error = getString(R.string.form_empty_message)
        }

        if (expDate.isEmpty()) {
            isError = true
            binding.etsellerEditProductExpireDate.error = getString(R.string.form_empty_message)
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
                originalPrice,
                stock,
                rack,
                DateTimeHelper.formatDate(prodDate),
                DateTimeHelper.formatDate(expDate)
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
                    binding.sellerEditProductNestedScrollView.gone()
                    binding.sellerEditProductprogressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.sellerEditProductNestedScrollView.visible()
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
                    binding.sellerEditProductNestedScrollView.gone()
                    binding.sellerEditProductprogressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.sellerEditProductNestedScrollView.visible()
                    binding.sellerEditProductprogressBar.gone()
                    showSuccessToast("Product edited successfully")
                    findNavController().popBackStack()
                }
            }
        }
    }

}