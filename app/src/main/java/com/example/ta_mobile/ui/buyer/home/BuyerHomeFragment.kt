package com.example.ta_mobile.ui.buyer.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerHomeBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.showToast
import org.koin.android.ext.android.inject

class BuyerHomeFragment : Fragment() {

    private lateinit var _binding : FragmentBuyerHomeBinding
    private val binding get() = _binding

    private val viewModel: BuyerHomeViewModel by inject()
    private lateinit var storeNearBuyerAdapter : BuyerHomeStoreNearBuyerAdapter

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

        viewModel.getStoreNearBuyer()
        setupAdapter()
        setupObserver()
        setupView()
        setupSearchView()

    }

    private fun setupObserver(){
        viewModel.getUserName().observe(viewLifecycleOwner){
            binding.buyerHomeUserNameTv.setText("Welcome, ${it}")
        }

        viewModel.storeNearBuyerResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    showToast(it.error)
                }
                NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    storeNearBuyerAdapter.setData(it.data.data)
                }
            }
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

    private fun setupSearchView(){
        binding.buyerHomeSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                val bundle = Bundle()
                bundle.putString("keyword", query)
                findNavController().navigate(R.id.action_buyerHomeFragment_to_buyerSearchStoreFragment, bundle)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun setupAdapter(){
        binding.buyerHomeStoreNearBuyerRv.layoutManager = LinearLayoutManager(requireContext())
        storeNearBuyerAdapter = BuyerHomeStoreNearBuyerAdapter{
            val bundle = Bundle()
            bundle.putString("storeId", it.id)
            findNavController().navigate(R.id.action_buyerHomeFragment_to_buyerStoreDetailFragment, bundle)
        }
        binding.buyerHomeStoreNearBuyerRv.adapter = storeNearBuyerAdapter
    }

}