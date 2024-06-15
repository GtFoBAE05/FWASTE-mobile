package com.example.ta_mobile.ui.auth.register.buyer

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentRegisterAsBuyerAdditionalBinding
import com.example.ta_mobile.ui.auth.register.RegisterViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showSuccessToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import org.koin.android.ext.android.inject

class RegisterAsBuyerAdditionalFragment : Fragment() {
    private lateinit var _binding : FragmentRegisterAsBuyerAdditionalBinding
    private val binding get() = _binding


    private lateinit var name: String
    private lateinit var email: String
    private lateinit var phoneNumber: String
    private lateinit var password: String
    private lateinit var referral:String

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val registerViewModel : RegisterViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = requireArguments().getString("name").toString()
        email = requireArguments().getString("email").toString()
        phoneNumber = requireArguments().getString("phoneNumber").toString()
        password = requireArguments().getString("password").toString()
        referral = requireArguments().getString("referral").toString()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterAsBuyerAdditionalBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
        setupObserver()
    }

    private fun checkLoginForm() {
        var isError = false
        val address = binding.etRegisterAddress.text.toString().trim()
        val location = binding.etRegisterLocation.text.toString().trim()


        if (address.isEmpty()) {
            isError = true
            binding.etRegisterAddress.error = getString(R.string.form_empty_message)
        }
        if (location.isEmpty()) {
            isError = true
            binding.etRegisterLocation.error = getString(R.string.form_empty_message)
        }


        if (!isError) {
            registerViewModel.registerAsBuyer(name, email, password, phoneNumber, address, location)
        }
    }

    private fun setupObserver(){
        registerViewModel.registerResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    binding.buyerRegisterProgressBar.gone()
                    binding.clBuyerRegisterAdditional.visible()
                    showErrorToast(it.error)
                }
                is NetworkResult.Loading -> {
                    binding.buyerRegisterProgressBar.visible()
                    binding.clBuyerRegisterAdditional.gone()
                }
                is NetworkResult.Success -> {
                    binding.buyerRegisterProgressBar.gone()
                    findNavController().navigate(R.id.action_registerAsBuyerAdditionalFragment_to_loginFragment)
                    showSuccessToast(it.data.message)
                }
            }
        }
    }


    private fun setupButton() {
        binding.btnConfirmRegisterAsBuyer.setOnClickListener {
            checkLoginForm()
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
                .addOnFailureListener {
                    showToast("error get location")
                }

        }

    }


}