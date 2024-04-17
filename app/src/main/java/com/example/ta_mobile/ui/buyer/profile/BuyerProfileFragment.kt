package com.example.ta_mobile.ui.buyer.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerProfileBinding
import org.koin.android.ext.android.inject

class BuyerProfileFragment : Fragment() {

    private lateinit var _binding: FragmentBuyerProfileBinding
    private val binding get() = _binding

    private val viewModel: BuyerProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButton()

    }

    private fun setupButton() {
        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_buyerProfileFragment_to_authActivity)
            requireActivity().finish()
        }
    }

}