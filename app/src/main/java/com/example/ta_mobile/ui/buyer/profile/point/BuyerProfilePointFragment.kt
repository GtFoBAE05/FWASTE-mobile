package com.example.ta_mobile.ui.buyer.profile.point

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.buyer.profile.BuyerPointResponseData
import com.example.ta_mobile.databinding.FragmentBuyerProfilePointBinding
import com.example.ta_mobile.ui.buyer.profile.BuyerProfileViewModel
import com.example.ta_mobile.ui.buyer.profile.voucher.BuyerProfileVoucherListAdapter
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject


class BuyerProfilePointFragment : Fragment() {

    private var _binding : FragmentBuyerProfilePointBinding? = null
    private val binding get() = _binding!!

    private val viewModel : BuyerProfileViewModel by inject()
    private lateinit var adapter: BuyerProfilePointListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBuyerProfilePointBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buyerProfilePointToolbar.title = "Point"
        binding.buyerProfilePointToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.getBuyerPointDetail()
        setupObserver()
        setupAdapter()
        setupButton()
    }

    private fun setupView(data : BuyerPointResponseData){
        binding.buyerPointProfileCurrentPointLevelTv.text = "${data.userPoint} pts - ${data.levelName}"
        binding.buyerProfilePointNeedToGainTv.text = "Gain ${data.pointToNextLevel} to reach next level"
    }

    private fun setupButton(){

        binding.buyerProfilePointHintScrollView.earnPointOrderCard.root.setOnClickListener {
            val title = TextView(requireContext())
            title.setText("Order by App")
            title.gravity = Gravity.CENTER
            title.textSize= 20F
            val alertDialog = AlertDialog.Builder(requireActivity())
            alertDialog.setCustomTitle(title)
            alertDialog.setMessage("Earn 1 point every 1 rupiah")
            alertDialog.setPositiveButton("Okay"){ dialog, which ->
                dialog.dismiss()
            }

            alertDialog.show()
        }

        binding.buyerProfilePointHintScrollView.earnPointMissionCard.root.setOnClickListener {
            val title = TextView(requireContext())
            title.setText("Mission Reward")
            title.gravity = Gravity.CENTER
            title.textSize= 20F
            val alertDialog = AlertDialog.Builder(requireActivity())
            alertDialog.setCustomTitle(title)
            alertDialog.setMessage("Complete mission within the App to earn FWASTE Points")
            alertDialog.setPositiveButton("Check Mission"){ dialog, which ->
                findNavController().navigate(R.id.action_buyerProfilePointFragment_to_buyerProfileMissionFragment)
            }

            alertDialog.show()
        }

        binding.buyerProfilePointHintScrollView.earnPointReferralCard.root.setOnClickListener {
            val title = TextView(requireContext())
            title.setText("Share Referral")
            title.gravity = Gravity.CENTER
            title.textSize= 20F
            val alertDialog = AlertDialog.Builder(requireActivity())
            alertDialog.setCustomTitle(title)
            alertDialog.setMessage("Share referral to other to earnt poin")
            alertDialog.setPositiveButton("Okay"){ dialog, which ->
                dialog.dismiss()
            }

            alertDialog.show()
        }

    }

    private fun setupAdapter(){
        binding.buyerProfilePointRewardRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerProfilePointListAdapter()

        binding.buyerProfilePointRewardRv.adapter = adapter

    }

    private fun setupObserver(){
        viewModel.userPointDetail.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    binding.buyerProfilePointProgressBar.gone()
                    showToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.buyerProfilePointProgressBar.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerProfilePointProgressBar.gone()
                    setupView(it.data.data)
                    adapter.setData(it.data.data.voucherReward)
                }
            }
        }
    }


}