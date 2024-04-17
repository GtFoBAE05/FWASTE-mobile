package com.example.ta_mobile.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentRegisterAsBinding

class RegisterAsFragment : Fragment() {

    private lateinit var _binding : FragmentRegisterAsBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterAsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButton()
    }

    private fun setupButton(){

        binding.btnRegisterAsBuyer.setOnClickListener {
            findNavController().navigate(R.id.action_registerAsFragment_to_registerAsBuyerFragment)
        }

        binding.btnRegisterAsSeller.setOnClickListener {
            findNavController().navigate(R.id.action_registerAsFragment_to_registerAsSellerFragment)
        }

        binding.btnToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerAsFragment_to_loginFragment)
        }

    }


}