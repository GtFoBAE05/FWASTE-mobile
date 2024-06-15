package com.example.ta_mobile.ui.buyer.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.auth.UserDetailResponseData
import com.example.ta_mobile.databinding.FragmentBuyerProfileBinding
import com.example.ta_mobile.ui.buyer.checkout.voucher.BuyerCheckoutVoucherListDialogFragment
import com.example.ta_mobile.ui.buyer.profile.referral.BuyerInputReferralFragment
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.CurrencyHelper
import com.example.ta_mobile.utils.helper.DateTimeHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class BuyerProfileFragment : Fragment() {

    private lateinit var _binding: FragmentBuyerProfileBinding
    private val binding get() = _binding

    private val viewModel: BuyerProfileViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buyerProfileToolbar.title = "Profile"

        viewModel.getUserDetail()
        setupButton()
        setupObserver()

    }

    private fun setupButton() {
        binding.vouchersLinearLayout.setOnClickListener {
            findNavController().navigate(R.id.action_buyerProfileFragment_to_buyerProfileVoucherFragment)
        }

        binding.pointLinearLayout.setOnClickListener {
            findNavController().navigate(R.id.action_buyerProfileFragment_to_buyerProfilePointFragment)
        }

        binding.btnInputReferral.setOnClickListener {
            val botSheet = BuyerInputReferralFragment()

            botSheet.show(requireActivity().supportFragmentManager, "tag")
        }

        binding.btnChangeProfile.setOnClickListener {
            findNavController().navigate(R.id.action_buyerProfileFragment_to_buyerProfileEditFragment)
        }

        binding.btnChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_buyerProfileFragment_to_buyerProfileEditPasswordFragment)
        }

        binding.btnMyFavouriteStore.setOnClickListener {
            findNavController().navigate(R.id.action_buyerProfileFragment_to_favouriteStoreFragment)
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_buyerProfileFragment_to_authActivity)
            requireActivity().finish()
        }
    }

    private fun setupView(data: UserDetailResponseData){
        Glide.with(requireContext()).load(data.imageUrl).into(binding.imgProfileCard)
        binding.profileEmail.text = data.fullname
        binding.profileLevel.text = data.level
        binding.profileBalance.text = "Balance ${CurrencyHelper.convertToRupiah(data.balance)}"
        binding.profileCreatedAt.text = DateTimeHelper.convertDate(data.member_since)

        binding.voucherTotalTv.text = data.totalVoucher.toString()
        binding.pointTotalTv.text = data.point.toString()

        binding.productSavedTv.text = "You have saved ${data.totalProduct } products"
        binding.priceSavedTv.text = "You have saved ${CurrencyHelper.convertToRupiah(data.totalPriceSaved) } "



    }

    private fun setupObserver(){
        viewModel.userDetailData.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                    binding.buyerProfileProgressBar.gone()
                }
                NetworkResult.Loading -> {
                    binding.buyerProfileScrollView.gone()
                    binding.buyerProfileProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerProfileScrollView.visible()
                    binding.buyerProfileProgressBar.gone()
                    setupView(it.data.data)

                    if (!it.data.data.referalFilled){
                        binding.btnInputReferral.visible()
                    }else{
                        binding.btnInputReferral.gone()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        activity?.viewModelStore?.clear()
    }

}