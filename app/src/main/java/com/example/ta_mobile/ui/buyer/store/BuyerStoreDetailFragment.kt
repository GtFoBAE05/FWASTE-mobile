package com.example.ta_mobile.ui.buyer.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerStoreDetailBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showSuccessToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import org.koin.android.ext.android.inject

class BuyerStoreDetailFragment : Fragment() {

    private lateinit var _binding: FragmentBuyerStoreDetailBinding
    private val binding get() = _binding

    private val viewModel: BuyerStoreDetailViewModel by inject()

    private lateinit var storeId: String

    private lateinit var adapter: BuyerStoreDetailProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storeId = requireArguments().getString("storeId").toString()

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_buyerStoreDetailFragment_to_buyerHomeFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerStoreDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStoreDetail(storeId)
        viewModel.getFavouriteStore()
        setupButton()
        setupAdapter()
        setupObserver()
    }

    private fun setupButton() {

        binding.buyerStoreDetailToolbar.title = "Store Detail"
        binding.buyerStoreDetailToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_buyerStoreDetailFragment_to_buyerHomeFragment)
        }

        binding.buyerStoreDetailGoToCartBtn.setOnClickListener {

        }
    }

    private fun setupView(
        imageUrl: String,
        storeName: String,
        storeAddress: String,
        storeOperationalHour: String
    ) {
        Glide.with(binding.root).load(imageUrl)
            .into(binding.buyerStoreDetailHeaderLayout.storeDetailItemImageView)
        binding.buyerStoreDetailHeaderLayout.storeDetailItemNameTv.text = storeName
        binding.buyerStoreDetailHeaderLayout.storeDetailItemLocationTv.text = storeAddress
        binding.buyerStoreDetailHeaderLayout.storeDetailItemOperationalHourTv.text = "Operational Hour: ${storeOperationalHour}"

    }

    private fun setupAdapter() {
        binding.buyerStoreDetailProductRv.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = BuyerStoreDetailProductAdapter {
            val bundle = Bundle()
            bundle.putString("productId", it.id)
            findNavController().navigate(R.id.action_buyerStoreDetailFragment_to_buyerProductFragment, bundle)
        }

        binding.buyerStoreDetailProductRv.adapter = adapter

    }

    private fun setupObserver() {
        viewModel.storeDetailResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    Log.e("TAG", "setupObserver: " + it.error, )
                    showErrorToast(it.error)
                    binding.buyerStoreDetailProgressBar.gone()
                }

                NetworkResult.Loading -> {
                    binding.buyerStoreDetailProgressBar.visible()
                    binding.buyerStoreDetailFAB.gone()
                    binding.buyerStoreDetailNestedScrollView.gone()
                    binding.buyerStoreDetailHeaderLayout.root.gone()
                }

                is NetworkResult.Success -> {
                    binding.buyerStoreDetailNestedScrollView.visible()
                    binding.buyerStoreDetailFAB.visible()
                    binding.buyerStoreDetailHeaderLayout.root.visible()
                    binding.buyerStoreDetailProgressBar.gone()
                    adapter.setData(it.data.data.products)
                    setupView(
                        it.data.data.storeImageUrl,
                        it.data.data.storeName,
                        it.data.data.storeAddress,
                        it.data.data.storeOperationalHour
                    )
                }
            }
        }

        viewModel.favStoreResult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    Log.e("TAG", "setupObserver: " + it.error, )
                    showErrorToast(it.error)
                    binding.buyerStoreDetailProgressBar.gone()
                }

                NetworkResult.Loading -> {
                    binding.buyerStoreDetailProgressBar.visible()
                    binding.buyerStoreDetailFAB.gone()
                    binding.buyerStoreDetailNestedScrollView.gone()
                }

                is NetworkResult.Success -> {
                    binding.buyerStoreDetailNestedScrollView.visible()
                    binding.buyerStoreDetailFAB.visible()
                    binding.buyerStoreDetailProgressBar.gone()
                    if (it.data.data.isEmpty()) {
                        binding.buyerStoreDetailFAB.setOnClickListener {
                            viewModel.addFavouriteStore(storeId)
                        }
                    }
                    binding.buyerStoreDetailFAB.icon = resources.getDrawable(R.drawable.baseline_favorite_border_24)
                    binding.buyerStoreDetailFAB.text = "Add to Favourite"

                    for (favStore in it.data.data) {

                        if (favStore.storeId == storeId) {
                            binding.buyerStoreDetailFAB.setOnClickListener {
                                viewModel.removeFavouriteStore(favStore.id)
                            }
                            binding.buyerStoreDetailFAB.icon = resources.getDrawable(R.drawable.baseline_favorite_24)
                            binding.buyerStoreDetailFAB.text = "Remove from Favourite"
                        }else{
                            binding.buyerStoreDetailFAB.setOnClickListener {
                                viewModel.addFavouriteStore(storeId)
                            }
                            binding.buyerStoreDetailFAB.icon = resources.getDrawable(R.drawable.baseline_favorite_border_24)
                            binding.buyerStoreDetailFAB.text = "Add to Favourite"
                        }
                    }

                }
            }
        }

        viewModel.addFavStoreResult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    Log.e("TAG", "setupObserver: " + it.error, )
                    showErrorToast(it.error)
                    binding.buyerStoreDetailProgressBar.gone()
                }

                NetworkResult.Loading -> {
                    binding.buyerStoreDetailProgressBar.visible()
                    binding.buyerStoreDetailFAB.gone()
                    binding.buyerStoreDetailNestedScrollView.gone()
                }

                is NetworkResult.Success -> {
                    binding.buyerStoreDetailProgressBar.gone()
                    binding.buyerStoreDetailNestedScrollView.visible()
                    binding.buyerStoreDetailFAB.visible()
                    binding.buyerStoreDetailFAB.icon = resources.getDrawable(R.drawable.baseline_favorite_24)
                    binding.buyerStoreDetailFAB.text = "Remove from Favourite"
                    showSuccessToast("Store added to favourite")
                    viewModel.subscribeTopic(storeId)
                }
            }
        }

        viewModel.removeFavStoreResult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    Log.e("TAG", "setupObserver: " + it.error, )
                    showErrorToast(it.error)
                    binding.buyerStoreDetailProgressBar.gone()
                }

                NetworkResult.Loading -> {
                    binding.buyerStoreDetailProgressBar.visible()
                }

                is NetworkResult.Success -> {
                    binding.buyerStoreDetailProgressBar.gone()
                    binding.buyerStoreDetailFAB.icon = resources.getDrawable(R.drawable.baseline_favorite_border_24)
                    showSuccessToast("Store removed from favourite")
                    binding.buyerStoreDetailFAB.text = "Add to Favourite"
                    viewModel.unsubscribeTopic(storeId)
                }
            }
        }
    }

}