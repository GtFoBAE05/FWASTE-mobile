package com.example.ta_mobile.ui.buyer.profile.mission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerProfileMissionBinding
import com.example.ta_mobile.ui.buyer.profile.BuyerProfileViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject


class BuyerProfileMissionFragment : Fragment() {

    private var _binding : FragmentBuyerProfileMissionBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BuyerProfileMissionAdapter

    private val viewModel : BuyerProfileViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerProfileMissionBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buyerProfileMissionToolbar.title = "Mission"
        binding.buyerProfileMissionToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getBuyerMission()
        setupAdapter()
        setupObserver()

    }

    private fun setupAdapter(){
        binding.buyerProfileMissionRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerProfileMissionAdapter {
            findNavController().navigate(R.id.action_buyerProfileMissionFragment_to_buyerHomeFragment)
        }

        binding.buyerProfileMissionRv.adapter = adapter
    }

    private fun setupObserver(){
        viewModel.userMission.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    binding.buyerProfilePB.gone()
                    showErrorToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.buyerProfilePB.visible()
                }
                is NetworkResult.Success -> {
                    binding.buyerProfilePB.gone()
                    adapter.setData(it.data.data)
                }
            }
        }
    }

}