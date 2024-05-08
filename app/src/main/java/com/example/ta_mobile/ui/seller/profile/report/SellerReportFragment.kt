package com.example.ta_mobile.ui.seller.profile.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentSellerReportBinding
import com.example.ta_mobile.ui.seller.profile.SellerProfileViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SellerReportFragment : Fragment() {

    private var _binding: FragmentSellerReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private val viewModel : SellerProfileViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sellerEditProfileToolbar.title = "Report"

        binding.sellerEditProfileToolbar.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.seller_report_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.SelectDateMenu -> {
                        val botSheet = SellerMonthYearSelectFragment()
                        botSheet.show(requireActivity().supportFragmentManager, "tag")
                        true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner)

        binding.sellerEditProfileToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
            activity?.viewModelStore?.clear()
        }

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = binding.sellerReportViewPager

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        viewPager.adapter = pagerAdapter

        val tabLayout = binding.sellerTabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when(position){
                0 -> tab.text = "Best Selling Product"
                1 -> tab.text = "Total Income"
            }
        }.attach()

        setupObserver()


    }

    private fun setupObserver(){
        viewModel.chartTitle.observe(viewLifecycleOwner) {
            binding.sellerEditProfileToolbar.title = it


        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment{
            return when(position){
                0 -> SellerBestSellingProductReportFragment()
                1 -> SellerTotalIncomeFragment()
                else -> SellerBestSellingProductReportFragment()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


