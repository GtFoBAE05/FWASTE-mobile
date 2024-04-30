package com.example.ta_mobile.ui.seller.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.auth.UserDetailResponseData
import com.example.ta_mobile.databinding.FragmentSellerProfileBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DateTimeHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SellerProfileFragment : Fragment() {

    private var _binding : FragmentSellerProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SellerProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sellerProfileToolbar.title = "Profile"

        viewModel.getUserDetail()
        setupObserver()
        setupButton()
    }

    private fun setupView(data : UserDetailResponseData){
        Glide.with(requireContext()).load(data.imageUrl).into(binding.imgProfileCard)
        binding.profileEmail.text = data.fullname
        binding.profileBalance.text = "Balance: ${CurrencyHelper.convertToRupiah(data.balance)}"
        binding.profileCreatedAt.text = DateTimeHelper.convertDate(data.member_since)
    }

    private fun setupButton() {


        binding.btnChangeProfile.setOnClickListener {
            findNavController().navigate(R.id.action_sellerProfileFragment_to_sellerEditProfileFragment)
        }

        binding.btnChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_sellerProfileFragment_to_sellerEditPasswordFragment)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_sellerProfileFragment_to_authActivity)
            requireActivity().finish()
        }
    }

    private fun setupObserver(){
        viewModel.userDetailData.observe(viewLifecycleOwner){
            viewModel.userDetailData.observe(viewLifecycleOwner){
                when(it){
                    is NetworkResult.Error -> {
                        showToast(it.error)
                        binding.sellerProfileProgressBar.gone()
                    }
                    NetworkResult.Loading -> {
                        binding.sellerProfileProgressBar.visible()
                    }
                    is NetworkResult.Success -> {
                        binding.sellerProfileProgressBar.gone()
                        setupView(it.data.data)
                    }
                }
            }
        }
    }
}