package com.example.ta_mobile.ui.buyer.favourite_store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentFavouriteStoreBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject

class FavouriteStoreFragment : Fragment() {

    private var _binding : FragmentFavouriteStoreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavouriteStoreViewModel by inject()
    private lateinit var adapter : BuyerFavouriteStoreAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buyerFavouriteStoreToolbar.title = "Favourite Store"
        binding.buyerFavouriteStoreToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.getFavouriteStore()

        setupAdapter()
        setupObserver()
    }

    private fun setupObserver(){


        viewModel.favStoreResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                }
                NetworkResult.Loading -> {
                    binding.buyerFavouriteStoreProgressBar.visible()
                    binding.buyerFavouriteStoreNestedScrollView.gone()
                }
                is NetworkResult.Success -> {
                    binding.buyerFavouriteStoreNestedScrollView.visible()
                    binding.buyerFavouriteStoreProgressBar.gone()
                    adapter.setData(it.data.data)
                }
            }
        }
    }

    private fun setupAdapter(){
        binding.favouriteStoreRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = BuyerFavouriteStoreAdapter{
            val bundle = Bundle()
            bundle.putString("storeId", it.storeId)
            findNavController().navigate(R.id.action_favouriteStoreFragment_to_buyerStoreDetailFragment, bundle)
        }
        binding.favouriteStoreRv.adapter = adapter
    }
}