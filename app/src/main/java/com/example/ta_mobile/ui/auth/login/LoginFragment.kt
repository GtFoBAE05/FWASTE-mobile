package com.example.ta_mobile.ui.auth.login

import android.Manifest
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentLoginBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } permissions.getOrDefault(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false) -> {
                // Only approximate location access granted.
                }permissions.getOrDefault(Manifest.permission.CAMERA, false) -> {
                // Only approximate location access granted.
                }permissions.getOrDefault(Manifest.permission.READ_EXTERNAL_STORAGE, false) -> {
                // Only approximate location access granted.
                }else -> {
                // No location access granted.
            }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            ))


        setupButton()
        checkLoginObserve()
    }

    private fun checkLoginForm() {
        var isError = false
        val email = binding.etSigninEmail.text.toString().trim()
        val password = binding.etSigninPassword.text.toString().trim()

        if (email.isEmpty()) {
            isError = true
            binding.etSigninEmail.error = getString(R.string.form_empty_message)
        }
        if (password.isEmpty()) {
            isError = true
            binding.etSigninPassword.error = getString(R.string.form_empty_message)
        }

        if (!isError) {
            viewModel.login(email, password)
        }
    }

    private fun setupButton() {
        binding.btnSignin.setOnClickListener {
            checkLoginForm()
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerAsFragment)
        }

    }

    private fun checkLoginObserve() {
        viewModel.userLogin.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is NetworkResult.Loading -> binding.cpiIndicator.visible()
                    is NetworkResult.Success -> {
                        binding.cpiIndicator.gone()
                        val isLoginSuccess = result.data.status
                        if (isLoginSuccess) {
                            showToast("Welcome back!")
                            if (result.data.data.role == "buyer") {
                                findNavController().navigate(R.id.action_loginFragment_to_buyerActivity)
                                requireActivity().finish()
                            } else {
                                findNavController().navigate(R.id.action_loginFragment_to_sellerActivity)
                                requireActivity().finish()
                            }

                        } else {
                            showToast(result.data.message)
                        }
                    }

                    is NetworkResult.Error -> {
                        showToast(result.error)
                        binding.cpiIndicator.gone()
                    }
                }
            }
        }
    }

}