package com.example.ta_mobile.ui.seller.profile.edit_profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.auth.UserDetailResponseData
import com.example.ta_mobile.databinding.FragmentSellerEditProfileBinding
import com.example.ta_mobile.ui.seller.profile.SellerProfileViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.DateTimeHelper
import com.example.ta_mobile.utils.helper.createCustomTempFile
import com.example.ta_mobile.utils.helper.downloadImageAsTempFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.inject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class SellerEditProfileFragment : Fragment() {

    private var _binding : FragmentSellerEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel : SellerProfileViewModel by inject()

    private var currentImageUri : Uri? = null
    private var getFile: File? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sellerEditProfileToolbar.title = "Edit Profile"
        binding.sellerEditProfileToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getUserDetail()
        setupObserver()
        setupButton()
        setupEditText()
    }

    private fun setupButton(){
        binding.buyerEditProfileImageView.setOnClickListener {
            startGallery()
        }

        binding.etRegisterLocation.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                showToast("Permission not allowed")
            }
            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        showToast("error get location")
                    else {
                        val lat = location.latitude
                        val lon = location.longitude
                        binding.etRegisterLocation.setText("${lat},${lon}")
                    }

                }
        }

        binding.btnContinue.setOnClickListener {
            checkUpdateProfileForm()
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
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.buyerEditProfileImageView.setImageURI(it)
        }
    }

    fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }


    private fun setupView(data : UserDetailResponseData){

        lifecycleScope.launch {
            getFile = downloadImageAsTempFile(requireContext(), data.imageUrl)
        }

        Glide.with(requireContext()).load(data.imageUrl).into(binding.buyerEditProfileImageView)
        binding.etRegisterName.setText(data.fullname)
        binding.etRegisterEmail.setText(data.email)
        binding.etRegisterPhoneNumber.setText(data.phoneNumber)
        binding.etRegisterAddress.setText(data.address)
        binding.etRegisterLocation.setText(data.location)
        binding.etRegisterOperationalOpenHour.setText(data.operationalHour.split("-")[0])
        binding.etRegisterOperationalCloseHour.setText(data.operationalHour.split("-")[1])
    }

    private fun setupObserver(){
        viewModel.userDetailData.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    showToast(it.error)
                    binding.buyerEditProfileProgressBar.gone()
                }
                NetworkResult.Loading -> {
                    binding.buyerEditProfileProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerEditProfileProgressBar.gone()
                    setupView(it.data.data)
                }
            }
        }

        viewModel.updateProfileResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    showToast(it.error)
                    binding.buyerEditProfileProgressBar.gone()
                }
                NetworkResult.Loading -> {
                    binding.buyerEditProfileProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerEditProfileProgressBar.gone()
                    findNavController().popBackStack()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupEditText() {
        binding.etRegisterOperationalOpenHour.setOnClickListener {
            val openTimePicker: MaterialTimePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Input operational open hour")
                .build()

            openTimePicker.show(requireActivity().supportFragmentManager, "TIME_PICKER")

            openTimePicker.addOnPositiveButtonClickListener {
                val formattedHour = DateTimeHelper.time24hFormatter(
                    openTimePicker.hour.toString(),
                    openTimePicker.minute.toString()
                )
                binding.etRegisterOperationalOpenHour.setText(formattedHour)
            }

        }

        binding.etRegisterOperationalCloseHour.setOnClickListener {
            val closeTimePicker: MaterialTimePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Input operational close hour")
                .build()


            closeTimePicker.show(requireActivity().supportFragmentManager, "TIME_PICKER")


            closeTimePicker.addOnPositiveButtonClickListener {
                val formattedHour = DateTimeHelper.time24hFormatter(
                    closeTimePicker.hour.toString(),
                    closeTimePicker.minute.toString()
                )
                binding.etRegisterOperationalCloseHour.setText(formattedHour)
            }

        }

        binding.etRegisterLocation.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                showToast("Permission not allowed")
            }
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        showToast("error get location")
                    else {
                        val lat = location.latitude
                        val lon = location.longitude
                        binding.etRegisterLocation.setText("${lat},${lon}")
                    }

                }
        }
    }

    private fun checkUpdateProfileForm() {
        var isError = false
        val name = binding.etRegisterName.text.toString()
        val email = binding.etRegisterEmail.text.toString().trim()
        val phoneNumber = binding.etRegisterPhoneNumber.text.toString().trim()
        val  address = binding.etRegisterAddress.text.toString().trim()
        val  location = binding.etRegisterLocation.text.toString().trim()
        val openHour = binding.etRegisterOperationalOpenHour.text.toString().trim()
        val closeHour = binding.etRegisterOperationalCloseHour.text.toString().trim()

        if (name.isEmpty()) {
            isError = true
            binding.etRegisterName.error = getString(R.string.form_empty_message)
        }

        if (email.isEmpty()) {
            isError = true
            binding.etRegisterEmail.error = getString(R.string.form_empty_message)
        }

        if (phoneNumber.isEmpty()) {
            isError = true
            binding.etRegisterPhoneNumber.error = getString(R.string.form_empty_message)
        }

        if (address.isEmpty()) {
            isError = true
            binding.etRegisterAddress.error = getString(R.string.form_empty_message)
        }

        if (location.isEmpty()) {
            isError = true
            binding.etRegisterLocation.error = getString(R.string.form_empty_message)
        }

        if (openHour.isEmpty()) {
            isError = true
            binding.etRegisterOperationalOpenHour.error = getString(R.string.form_empty_message)
        }

        if (closeHour.isEmpty()) {
            isError = true
            binding.etRegisterOperationalCloseHour.error = getString(R.string.form_empty_message)
        }


        Log.e("TAG", "checkUpdateProfileForm: " + name, )

        if (!isError) {
            val file = getFile as File
            val image = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imagePart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image",
                file.name,
                image
            )

            viewModel.updateProfile(imagePart, name, email, phoneNumber, address, location, "${openHour}-${closeHour}")

        }
    }


}