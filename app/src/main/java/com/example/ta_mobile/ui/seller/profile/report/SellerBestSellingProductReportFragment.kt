package com.example.ta_mobile.ui.seller.profile.report

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ta_mobile.R
import com.example.ta_mobile.data.source.remote.response.seller.report.BestSellingProductResponseData
import com.example.ta_mobile.databinding.FragmentSellerBestSellingProductReportBinding
import com.example.ta_mobile.ui.seller.profile.SellerProfileViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class SellerBestSellingProductReportFragment : Fragment() {
    private var _binding: FragmentSellerBestSellingProductReportBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SellerProfileViewModel by activityViewModel()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerBestSellingProductReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBestSellingProduct()

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.bestSellingProductResult.observe(viewLifecycleOwner) {

            when (it) {
                is NetworkResult.Loading -> {
                binding.bestSellingProductPB.visible()
                }

                is NetworkResult.Success -> {
                    binding.bestSellingProductPB.gone()


                    setData(it.data.data)



                }

                is NetworkResult.Error -> {
                    binding.bestSellingProductPB.gone()
                    showToast(it.error)
                    Log.e("TAG", "setupObserver: " + it.error)

                }
            }

        }




    }

    private fun setData(dataEntry: List<BestSellingProductResponseData>) {
        val entries: ArrayList<PieEntry> = ArrayList<PieEntry>()

        val dataMapped: MutableMap<String, Int> = mutableMapOf()

        dataEntry.forEach {
            val existingTotalSold = dataMapped[it.name] ?: 0
            dataMapped[it.name] = existingTotalSold + it.totalSold
        }

        dataMapped.forEach { (name, totalSold) ->
            entries.add(PieEntry(totalSold.toFloat(), name))
        }

        val dataSet = PieDataSet(entries, "Best Selling Product")
        dataSet.setDrawIcons(false)
        dataSet.setSliceSpace(3f)
        dataSet.setIconsOffset(MPPointF(0F, 40F))
        dataSet.setSelectionShift(5f)

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.setColors(colors)
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        binding.pieChartView.setData(data)

        // undo all highlights
        binding.pieChartView.highlightValues(null)
        binding.pieChartView.invalidate()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}