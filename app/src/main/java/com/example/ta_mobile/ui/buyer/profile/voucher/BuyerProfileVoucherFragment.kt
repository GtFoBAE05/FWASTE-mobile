package com.example.ta_mobile.ui.buyer.profile.voucher

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerProfileVoucherBinding
import com.example.ta_mobile.ui.buyer.profile.BuyerProfileViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject


class BuyerProfileVoucherFragment : Fragment() {
    private var _binding : FragmentBuyerProfileVoucherBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BuyerProfileVoucherListAdapter

    private val viewModel: BuyerProfileViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerProfileVoucherBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buyerOrderStatusToolbar.title = "Owned Voucher"
        binding.buyerOrderStatusToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getUserOwnedVoucher()

        setupAdapter()
        setupObserver()
        setupButton()

    }

    private fun setupButton(){
        binding.buyerProfileVoucherGetMoreVoucherHint.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle("Get More Voucher")
            alertDialog.setMessage("You can obtain the voucher by completing available missions. Please visit the missions page to see the list of missions you can complete.")
            alertDialog.setPositiveButton("Go~") { dialog, _ ->
                findNavController().navigate(R.id.action_buyerProfileVoucherFragment_to_buyerProfileMissionFragment)
            }

            alertDialog.setNegativeButton("Close", null)

            alertDialog.show()
        }
    }

    private fun setupAdapter(){
        binding.buyerProfilVoucherRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerProfileVoucherListAdapter()

        binding.buyerProfilVoucherRv.adapter = adapter

    }

    private fun setupObserver(){
        viewModel.userVoucherList.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    binding.buyerProfileVoucherProgressBar.gone()
                    showErrorToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.buyerProfileVoucherNestedScrollView.gone()
                    binding.buyerProfileVoucherProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerProfileVoucherNestedScrollView.visible()
                    binding.buyerProfileVoucherProgressBar.gone()
                    if(it.data.data.isEmpty()){
                    }else{
                        adapter.setData(it.data.data)
                    }
                }
            }
        }
    }


}