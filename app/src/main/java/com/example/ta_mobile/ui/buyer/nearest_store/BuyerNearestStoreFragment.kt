package com.example.ta_mobile.ui.buyer.nearest_store

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.buyer.nearest_store.NearestStoreResponseData
import com.example.ta_mobile.databinding.FragmentBuyerNearestStoreBinding
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showErrorToast
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.EmojiHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import org.koin.android.ext.android.inject


class BuyerNearestStoreFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private var _binding: FragmentBuyerNearestStoreBinding? = null
    private val binding get() = _binding!!

    private val viewmodel: BuyerNearestStoreViewModel by inject()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val boundsBuilder = LatLngBounds.Builder()

    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap

        googleMap.uiSettings.isMapToolbarEnabled = true

        getMyLocation()
        setupMapClick()
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
            mMap.isMyLocationEnabled = true

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
                .addOnSuccessListener { location: Location? ->
                    if (location == null)
                        showErrorToast("error get location")
                    else {
                        val lat = location.latitude
                        val lon = location.longitude
                        boundsBuilder.include(LatLng(lat, lon))
                        viewmodel.getNearestStore(lat, lon, 10.0)

                        binding.buyerOrderMapsToolbar.addMenuProvider(object : MenuProvider{
                            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                                menuInflater.inflate(R.menu.buyer_map_radius, menu)
                            }

                            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                                return when(menuItem.itemId){
                                    R.id.radius10km -> {
                                        mMap.clear()
                                        viewmodel.getNearestStore(lat, lon, 10.0)
                                        showToast("Store radius selected to near 10 km")
                                        true
                                    }
                                    R.id.radius20km -> {
                                        mMap.clear()
                                        viewmodel.getNearestStore(lat, lon, 20.0)
                                        showToast("Store radius selected to near 20 km")
                                        true
                                    }
                                    R.id.radius50km -> {
                                        mMap.clear()
                                        viewmodel.getNearestStore(lat, lon, 50.0)
                                        showToast("Store radius selected to near 50 km")
                                        true
                                    }
                                    R.id.radius100km -> {
                                        mMap.clear()
                                        viewmodel.getNearestStore(lat, lon,100.0)
                                        showToast("Store radius selected to near 100 km")
                                        true
                                    }
                                    else -> false
                                }
                            }

                        }, viewLifecycleOwner)

                    }

                }

        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuyerNearestStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.buyerNearestStoreMap) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

//        val zoomControlView = mapFragment?.view?.findViewById<View>("1".toInt())
//        val layoutParams = zoomControlView?.layoutParams as RelativeLayout.LayoutParams
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, )
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)

        binding.buyerOrderMapsToolbar.title = "Nearest Store"

        setupObserver()



    }



    private fun setupObserver(){
        viewmodel.userNearestStore.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Error -> {
                    showErrorToast(it.error)
                }
                NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    val nearestStore = it.data
                    val store = nearestStore.data
                    for (i in store.indices){
                        val storeLocation = LatLng(store[i].location.split(",")[0].toDouble(), store[i].location.split(",")[1].toDouble())

                        mMap.addMarker(MarkerOptions().position(storeLocation).title(store[i].fullname))
                        boundsBuilder.include(storeLocation)

                    }

                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            300
                        )
                    )
                    setupMarker(it.data.data)

                }
            }
        }
    }

    private fun setupMarker(data : List<NearestStoreResponseData>){
        mMap.setOnMarkerClickListener { marker ->
            binding.storeInfoCard.root.visible()
            val store = data.find { it.fullname == marker.title }
            Glide.with(requireContext()).load(store?.imageUrl).into(binding.storeInfoCard.sellerInfoImageView)
            binding.storeInfoCard.sellerInfoName.text = store?.fullname
            binding.storeInfoCard.sellerInfoAddress.text = store?.address
            binding.storeInfoCard.sellerInfoRating.text = EmojiHelper.getEmoji(0x2605) + store?.rating.toString()
            false
        }
    }

    private fun setupMapClick(){
        mMap.setOnMapClickListener {
            binding.storeInfoCard.root.gone()
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}