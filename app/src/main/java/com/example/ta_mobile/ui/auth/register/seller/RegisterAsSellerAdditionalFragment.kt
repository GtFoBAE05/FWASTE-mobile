package com.example.ta_mobile.ui.auth.register.seller

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentRegisterAsBuyerAdditionalBinding
import com.example.ta_mobile.databinding.FragmentRegisterAsSellerAdditionalBinding
import com.example.ta_mobile.ui.auth.register.RegisterViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.DateTimeHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import org.koin.android.ext.android.inject
import java.util.Calendar


class RegisterAsSellerAdditionalFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterAsSellerAdditionalBinding
    private val binding get() = _binding


    private lateinit var name: String
    private lateinit var email: String
    private lateinit var phoneNumber: String
    private lateinit var password: String

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val registerViewModel : RegisterViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = requireArguments().getString("name").toString()
        email = requireArguments().getString("email").toString()
        phoneNumber = requireArguments().getString("phoneNumber").toString()
        password = requireArguments().getString("password").toString()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentRegisterAsSellerAdditionalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEditText()
        setupButton()
        setupObserver()
    }

    private fun checkLoginForm() {
        var isError = false
        val address = binding.etRegisterAddress.text.toString().trim()
        val location = binding.etRegisterLocation.text.toString().trim()
        val openHour = binding.etRegisterOperationalOpenHour.text.toString().trim()
        val closeHour = binding.etRegisterOperationalCloseHour.text.toString().trim()


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

        if (!isError) {
            registerViewModel.registerAsSeller(name, email, password, phoneNumber, address, location, "${openHour}-${closeHour}")
        }
    }

    private fun setupObserver(){
        registerViewModel.registerResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    binding.sellerRegisterProgressBar.gone()
                    showToast(it.error)
                }
                is NetworkResult.Loading -> {
                    binding.sellerRegisterProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.sellerRegisterProgressBar.gone()
                    findNavController().popBackStack()
                    showToast(it.data.message)
                }
            }
        }
    }

    private fun setupButton() {
        binding.btnConfirmRegisterAsSeller.setOnClickListener {
            checkLoginForm()
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

            openTimePicker.show(requireActivity().supportFragmentManager , "TIME_PICKER")

            openTimePicker.addOnPositiveButtonClickListener {
                val formattedHour = DateTimeHelper.time24hFormatter(openTimePicker.hour.toString(), openTimePicker.minute.toString())
                binding.etRegisterOperationalOpenHour.setText(formattedHour)
            }

        }

        binding.etRegisterOperationalCloseHour.setOnClickListener {
            val closeTimePicker: MaterialTimePicker = MaterialTimePicker
                .Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Input operational close hour")
                .build()


            closeTimePicker.show(requireActivity().supportFragmentManager , "TIME_PICKER")


            closeTimePicker.addOnPositiveButtonClickListener {
                val formattedHour = DateTimeHelper.time24hFormatter(closeTimePicker.hour.toString(), closeTimePicker.minute.toString())
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




    }


}