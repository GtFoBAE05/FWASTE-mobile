package com.example.ta_mobile.ui.seller.profile.edit_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentSellerEditPasswordBinding
import com.example.ta_mobile.ui.seller.profile.SellerProfileViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showSuccessToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject

class SellerEditPasswordFragment : Fragment() {
    private var _binding: FragmentSellerEditPasswordBinding? = null
    private val binding get() = _binding!!

    private val viewModel : SellerProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerEditPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buyerEditProfilePasswordToolbar.title = "Change Password"
        binding.buyerEditProfilePasswordToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        setupObserver()
        setupButton()
    }

    private fun setupButton(){
        binding.btnContinue.setOnClickListener {
            checkUpdateProfileForm()
        }
    }

    private fun checkUpdateProfileForm() {
        var isError = false
        val password = binding.etBuyerEditPassword.text.toString()
        val passwordConfirmation = binding.etBuyerEditPasswordConfirmation.text.toString().trim()

        if (password.isEmpty()) {
            isError = true
            binding.etBuyerEditPassword.error = getString(R.string.form_empty_message)
        }

        if (passwordConfirmation.isEmpty()) {
            isError = true
            binding.etBuyerEditPasswordConfirmation.error = getString(R.string.form_empty_message)
        }

        if (!password.equals(passwordConfirmation)) {
            isError = true
            binding.etBuyerEditPassword.error = "Password not match"
            binding.etBuyerEditPasswordConfirmation.error = "Password not match"
        }



        if (!isError) {
            viewModel.updateUserPassword(password)
        }
    }

    private fun setupObserver() {
        viewModel.updateUserPasswordesult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                    binding.buyerEditPasswordPB.gone()
                }

                NetworkResult.Loading -> {
                    binding.buyerEditPasswordPB.visible()
                }

                is NetworkResult.Success -> {
                    binding.buyerEditPasswordPB.gone()
                    showSuccessToast("Success change password")
                    findNavController().popBackStack()
                }
            }
        }
    }

}