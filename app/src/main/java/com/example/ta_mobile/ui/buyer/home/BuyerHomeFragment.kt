package com.example.ta_mobile.ui.buyer.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerHomeBinding
import org.koin.android.ext.android.inject

class BuyerHomeFragment : Fragment() {

    private lateinit var _binding : FragmentBuyerHomeBinding
    private val binding get() = _binding

    private val viewModel: BuyerHomeViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buyerHomeToolbar.setTitle("Explore")
        setupObserver()
        setupView()

    }

    private fun setupObserver(){
        viewModel.getUserName().observe(viewLifecycleOwner){
            binding.buyerHomeUserNameTv.setText("Welcome, ${it}")
        }
    }

    private fun setupView(){
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel("https://bit.ly/2YoJ77H", ))
        imageList.add(SlideModel("https://bit.ly/2BteuF2", ))
        imageList.add(SlideModel("https://bit.ly/3fLJf72", ))
        binding.buyerHomeImageSlider.setImageList(imageList, ScaleTypes.FIT)

        binding.buyerHomeImageSlider.setOnClickListener {

        }

    }

}