package com.example.ta_mobile.ui.buyer.home

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentBuyerHomeBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import org.koin.android.ext.android.inject

class BuyerHomeFragment : Fragment() {

    private lateinit var _binding: FragmentBuyerHomeBinding
    private val binding get() = _binding

    private val viewModel: BuyerHomeViewModel by inject()
    private lateinit var storeNearBuyerAdapter: BuyerHomeStoreNearBuyerAdapter

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

        viewModel.getUserDetail()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


        setupAdapter()
        setupView()
        setupSearchView()
        setupButton()

        getMyLocation()
        setupObserver()

    }

    private fun setupObserver() {
        viewModel.getUserName().observe(viewLifecycleOwner) {
            binding.buyerHomeUserNameTv.text = "Welcome, ${it}"
        }

        viewModel.storeNearBuyerResult.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                }

                NetworkResult.Loading -> {
                    binding.buyerHomeNestedScrollView.gone()
                }

                is NetworkResult.Success -> {
                    storeNearBuyerAdapter.setData(it.data.data)
                    binding.buyerHomeNestedScrollView.visible()
                }
            }
        }

        viewModel.userDetailData.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                }

                NetworkResult.Loading -> {
                    binding.buyerHomeNestedScrollView.gone()
                }

                is NetworkResult.Success -> {
                    binding.buyerHomeNestedScrollView.visible()
                    binding.voucherTotalTv.text = it.data.data.totalVoucher.toString()
                    binding.pointTotalTv.text = it.data.data.point.toString()
                }
            }
        }
    }

    private fun setupView() {
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.food_waste_ads_01))
        imageList.add(SlideModel(R.drawable.express_delivery_ads_02))
        binding.buyerHomeImageSlider.setImageList(imageList, ScaleTypes.FIT)

        binding.buyerHomeImageSlider.setOnClickListener {
        }

    }

    private fun setupSearchView() {
        binding.buyerHomeSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                val bundle = Bundle()
                bundle.putString("keyword", query)
                findNavController().navigate(
                    R.id.action_buyerHomeFragment_to_buyerSearchStoreFragment,
                    bundle
                )
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun setupAdapter() {
        binding.buyerHomeStoreNearBuyerRv.layoutManager = LinearLayoutManager(requireContext())
        storeNearBuyerAdapter = BuyerHomeStoreNearBuyerAdapter {
            val bundle = Bundle()
            bundle.putString("storeId", it.id)
            findNavController().navigate(
                R.id.action_buyerHomeFragment_to_buyerStoreDetailFragment,
                bundle
            )
        }
        binding.buyerHomeStoreNearBuyerRv.adapter = storeNearBuyerAdapter
    }

    private fun setupButton() {
        binding.voucherTotalTv.setOnClickListener {
            findNavController().navigate(R.id.action_buyerHomeFragment_to_buyerProfileVoucherFragment)
        }

        binding.pointTotalTv.setOnClickListener {
            findNavController().navigate(R.id.action_buyerHomeFragment_to_buyerProfilePointFragment)
        }

        binding.heavyMealsCategory.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("keyword", "heavy meals")
            findNavController().navigate(
                R.id.action_buyerHomeFragment_to_buyerSearchProductFragment,
                bundle
            )
        }

        binding.snackCategory.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("keyword", "snack")
            findNavController().navigate(
                R.id.action_buyerHomeFragment_to_buyerSearchProductFragment,
                bundle
            )
        }

        binding.breadCategory.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("keyword", "bread")
            findNavController().navigate(
                R.id.action_buyerHomeFragment_to_buyerSearchProductFragment,
                bundle
            )
        }

        binding.seafoodCategory.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("keyword", "seafood")
            findNavController().navigate(
                R.id.action_buyerHomeFragment_to_buyerSearchProductFragment,
                bundle
            )
        }

        binding.drinkCategory.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("keyword", "drink")
            findNavController().navigate(
                R.id.action_buyerHomeFragment_to_buyerSearchProductFragment,
                bundle
            )
        }

        binding.thirtyPercentCategory.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("keyword", "30")
            findNavController().navigate(
                R.id.action_buyerHomeFragment_to_buyerSearchProductByDiscountFragment,
                bundle
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener) =
                        CancellationTokenSource().token

                    override fun isCancellationRequested() = false
                })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        showErrorToast("error get location")
                    else {
                        val lat = location.latitude
                        val lon = location.longitude
                        viewModel.getNearestStore(lat, lon, 50.0)

                    }

                }

        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onDestroyView() {
        viewModel.storeNearBuyerResult.removeObservers(requireParentFragment().viewLifecycleOwner)
        viewModel.userDetailData.removeObservers(requireParentFragment().viewLifecycleOwner)

        super.onDestroyView()
    }

}