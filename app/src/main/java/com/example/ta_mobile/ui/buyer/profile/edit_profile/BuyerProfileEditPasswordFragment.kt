package com.example.ta_mobile.ui.buyer.profile.edit_profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerProfileEditPasswordBinding
import com.example.ta_mobile.ui.buyer.profile.BuyerProfileViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.inject
import java.io.File


class BuyerProfileEditPasswordFragment : Fragment() {

    private var _binding : FragmentBuyerProfileEditPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel : BuyerProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuyerProfileEditPasswordBinding.inflate(layoutInflater, container,false)
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
                    showToast(it.error)
                    binding.buyerEditPasswordPB.gone()
                }

                NetworkResult.Loading -> {
                    binding.buyerEditPasswordPB.visible()
                }

                is NetworkResult.Success -> {
                    binding.buyerEditPasswordPB.gone()
                    showToast("Success change password")
                    findNavController().navigate(R.id.action_buyerProfileEditPasswordFragment_to_buyerHomeFragment)
                }
            }
        }
    }


}