package com.example.ta_mobile.ui.auth.register.seller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentRegisterAsSellerBinding


class RegisterAsSellerFragment : Fragment() {

    private lateinit var _binding : FragmentRegisterAsSellerBinding
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterAsSellerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButton()
    }

    private fun checkLoginForm() {
        var isError = false
        val name = binding.etRegisterName.text.toString().trim()
        val email = binding.etRegisterEmail.text.toString().trim()
        val phoneNumber = binding.etRegisterPhoneNumber.text.toString().trim()
        val password = binding.etRegisterPassword.text.toString().trim()

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

        if (password.isEmpty()) {
            isError = true
            binding.etRegisterPassword.error = getString(R.string.form_empty_message)
        }

        if (!isError) {
            val bundle= Bundle()
            bundle.putString("name", name)
            bundle.putString("email", email)
            bundle.putString("phoneNumber", phoneNumber)
            bundle.putString("password", password)
            findNavController().navigate(R.id.action_registerAsSellerFragment_to_registerAsSellerAdditionalFragment, bundle)
        }
    }

    private fun setupButton(){
        binding.btnContinue.setOnClickListener {
            checkLoginForm()
        }

        binding.btnToLogin.setOnClickListener {
            findNavController().navigate(RegisterAsSellerFragmentDirections.actionRegisterAsSellerFragmentToLoginFragment())
        }

    }


}