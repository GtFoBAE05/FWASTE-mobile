package com.example.ta_mobile.ui.seller.home.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentSellerSendNotificationBinding
import com.example.ta_mobile.ui.seller.home.SellerHomeViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showSuccessToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject


class SellerSendNotificationFragment : Fragment() {

    private var _binding: FragmentSellerSendNotificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SellerHomeViewModel by inject()

    private var productId: String? = null
    private var productName: String? = null
    private var imageUrl: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerSendNotificationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sellerSendNotificationToolbar.title = "Send Notification"
        binding.sellerSendNotificationToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        setFragmentResultListener("product") { requestKey, bundle ->
             productId = bundle.getString("productId")
             productName = bundle.getString("productName")
             imageUrl = bundle.getString("imageUrl")
            setupView()
        }

        setupButton()
        setupObserver()
        setupView()
    }

    private fun setupButton() {
        binding.chooseProductCard.root.setOnClickListener {
            findNavController().navigate(R.id.action_sellerSendNotificationFragment_to_sellerSendProductListProductFragment)
        }

        binding.sendNotificationButton.setOnClickListener {
            checkSendNotificationForm()
        }
    }

    private fun setupView(){
        if(productId != null){
            binding.chooseProductCard.chooseProductImageView.visible()
            Glide.with(binding.root)
                .load(imageUrl)
                .into(binding.chooseProductCard.chooseProductImageView)
            binding.chooseProductCard.chooseProductTitleTv.text = productName
        }
    }

    private fun checkSendNotificationForm() {
        var isError = false
        val title = binding.etSellerSendNotificationTitle.text.toString()
        val message = binding.etSellerSendNotificationDescription.text.toString().trim()

        if (title.isEmpty()) {
            isError = true
            binding.etSellerSendNotificationTitle.error = getString(R.string.form_empty_message)
        }

        if (message.isEmpty()) {
            isError = true
            binding.etSellerSendNotificationDescription.error = getString(R.string.form_empty_message)
        }

        if(productId == null){
            isError = true
            showToast("Please choose product")
        }





        if (!isError) {
            viewModel.sendNotification(title, message, productId.toString())
        }
    }

    private fun setupObserver(){
        viewModel.sendNotificationResult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                    binding.sellerSendNotificationProgressBar.gone()
                }

                NetworkResult.Loading -> {
                    binding.sellerSendNotificationProgressBar.visible()
                }

                is NetworkResult.Success -> {
                    binding.sellerSendNotificationProgressBar.gone()
                    showSuccessToast("Success send notification")
                    findNavController().popBackStack()
                }
            }
        }
    }
}

