package com.example.ta_mobile.ui.seller.profile.report

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import com.example.ta_mobile.data.source.remote.response.seller.report.TotalIncomeResponseData
import com.example.ta_mobile.databinding.FragmentSellerTotalIncomeTableViewBinding
import com.example.ta_mobile.ui.seller.profile.SellerProfileViewModel
import com.example.ta_mobile.utils.NetworkResult
import com.example.ta_mobile.utils.extension.gone
import com.example.ta_mobile.utils.extension.showToast
import com.example.ta_mobile.utils.extension.visible
import com.example.ta_mobile.utils.helper.CurrencyHelper
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.DateFormatSymbols
import java.util.Locale


class SellerTotalIncomeTableViewFragment : Fragment() {

    private lateinit var _binding: FragmentSellerTotalIncomeTableViewBinding
    private val binding get() = _binding

    private val viewModel: SellerProfileViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerTotalIncomeTableViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.totalIncomeResult.observe(viewLifecycleOwner) { it ->

            when (it) {
                is NetworkResult.Loading -> {
                    binding.totalIncomeTableViewPB.visible()
                    binding.sellerTotalIncomeTableLayout.gone()
                }

                is NetworkResult.Success -> {
                    binding.totalIncomeTableViewPB.gone()
                    binding.sellerTotalIncomeTableLayout.visible()
                    setData(it.data.data)

                }

                is NetworkResult.Error -> {
                    binding.totalIncomeTableViewPB.gone()
                    binding.sellerTotalIncomeTableLayout.visible()
                    showToast(it.error)
                    Log.e("TAG", "setupObserver: " + it.error)
                }
            }

        }

    }

    private fun setData(abc: List<TotalIncomeResponseData>) {
        binding.sellerTotalIncomeTableLayout.removeViews(1, binding.sellerTotalIncomeTableLayout.childCount - 1)
        for ((index, item) in abc.withIndex()) {
            val row = TableRow(requireContext()).apply {
                layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
                if (index % 2 == 0) {
                    setBackgroundColor(0xFF6F9C33.toInt())
                }else{
                    setBackgroundColor(0xFF92C94A.toInt())
                }
            }

            val noTextView = TextView(requireContext()).apply {
                text = (index + 1).toString()
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
//                setPadding(10, 10, 10, 10)
            }

            val monthTextView = TextView(requireContext()).apply {
                text = DateFormatSymbols(Locale.US).months[item.month.toInt()-1]
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f)
//                setPadding(10, 10, 10, 10)
            }

            val totalTextView = TextView(requireContext()).apply {
                text = CurrencyHelper.convertToRupiah(item.income)
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f)
//                setPadding(10, 10, 10, 10)
            }

            row.addView(noTextView)
            row.addView(monthTextView)
            row.addView(totalTextView)
            binding.sellerTotalIncomeTableLayout.addView(row)
        }
    }



}