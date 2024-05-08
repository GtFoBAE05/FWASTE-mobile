package com.example.ta_mobile.ui.seller.profile.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ta_mobile.data.source.remote.response.seller.report.TotalIncomeResponseData
import com.example.ta_mobile.databinding.FragmentSellerTotalIncomeBinding
import com.example.ta_mobile.ui.seller.profile.SellerProfileViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.DateFormatSymbols
import java.util.Locale


class SellerTotalIncomeFragment : Fragment() {

    private var _binding: FragmentSellerTotalIncomeBinding? = null
    private val binding get() = _binding!!


    private val viewModel: SellerProfileViewModel by activityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSellerTotalIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getTotalIncome()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.totalIncomeResult.observe(viewLifecycleOwner) { it ->

            when (it) {
                is NetworkResult.Loading -> {
                    binding.totalIncomePB.visible()
                }

                is NetworkResult.Success -> {
                    binding.totalIncomePB.gone()

                    setData(it.data.data)

                }

                is NetworkResult.Error -> {
                    binding.totalIncomePB.gone()
                    showToast(it.error)
                    Log.e("TAG", "setupObserver: " + it.error)
                }
            }

        }



    }


    private fun setData(abc: List<TotalIncomeResponseData>) {
        val entries = ArrayList<BarEntry>()
        abc.forEach {
            entries.add(BarEntry(it.month.toFloat(), it.income.toFloat()))
        }

        val dataset = BarDataSet(entries, "# of Calls")
        val data = BarData(dataset)

        val xAxisFormatter: ValueFormatter = DayAxisValueFormatter(binding.pieChartView)
        val xAxis: XAxis = binding.pieChartView.getXAxis()
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.setGranularity(1f)

        xAxis.setLabelCount(7)
        xAxis.valueFormatter = xAxisFormatter

        binding.pieChartView.setData(data);
        binding.pieChartView.invalidate()
    }


    class DayAxisValueFormatter(private val chart: BarLineChartBase<*>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return DateFormatSymbols(Locale.US).getMonths()[value.toInt()-1]
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}