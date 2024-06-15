package com.example.ta_mobile.ui.seller.profile.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.example.ta_mobile.R
import com.example.ta_mobile.databinding.FragmentSellerMonthYearSelectBinding
import com.example.ta_mobile.ui.seller.profile.SellerProfileViewModel
import com.example.ta_mobile.utils.extension.showToast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale


class SellerMonthYearSelectFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentSellerMonthYearSelectBinding? = null
    private val binding get() = _binding!!

    private val viewModel : SellerProfileViewModel by activityViewModels()

    private var startMonth = 0
    private var startYear = 0
    private var endMonth = 0
    private var endYear = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSellerMonthYearSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupButton()
    }

    private fun setupButton(){
        binding.etStartDate.setOnClickListener {
            val dialog = MonthYearPickerDialog.Builder(
                requireActivity(),
                R.style.Style_MonthYearPickerDialog_Orange,
                { year, month ->
                    startMonth = month
                    startYear = year
                    binding.etStartDate.setText("${DateFormatSymbols(Locale.US).months[month]}/$year")
                },
                2024,
                Calendar.JULY
            )
                .setNegativeButton("Return")
                .setPositiveButton("OK")
                .build()

            dialog.setTitle("Select Start Month and Year")

            dialog.show()
        }

        binding.etEndDate.setOnClickListener {
            val dialog = MonthYearPickerDialog.Builder(
                requireActivity(),
                R.style.Style_MonthYearPickerDialog_Orange,
                { year, month ->
                    endMonth = month
                    endYear = year
                    binding.etEndDate.setText("${DateFormatSymbols(Locale.US).months[month]}/$year")
                },
                2024,
                Calendar.JULY
            )
                .setNegativeButton("Return")
                .setPositiveButton("OK")
                .build()

            dialog.setTitle("Select End Month and Year")

            dialog.show()
        }

        binding.ConfirmDateBtn.setOnClickListener {
            if (startMonth == 0 && startYear == 0 && endMonth == 0 && endYear == 0) {
                showToast("Please select start and end date")
                return@setOnClickListener
            }
            viewModel.setMonthYear(
                startMonth = startMonth+1,
                endMonth = endMonth+1,
                startYear = startYear,
                endYear = endYear)
            viewModel.getBestSellingProduct(
                startMonth = startMonth+1,
                endMonth = endMonth+1,
                startYear = startYear,
                endYear = endYear
            )
            viewModel.getTotalIncome(
                startMonth = startMonth+1,
                endMonth = endMonth+1,
                startYear = startYear,
                endYear = endYear
            )

            viewModel.setChartTitle("${DateFormatSymbols(Locale.US).months[startMonth]}/$startYear - ${DateFormatSymbols(Locale.US).months[endMonth]}/$endYear")
            dismiss()
        }

        binding.sellerResetFilterButton.setOnClickListener {
            viewModel.setMonthYear(
                startMonth = 0,
                endMonth = 0,
                startYear = 0,
                endYear = 0)
            viewModel.getBestSellingProduct(
                startMonth = 0,
                endMonth = 0,
                startYear = 0,
                endYear = 0
            )
            viewModel.getTotalIncome(
                startMonth = 0,
                endMonth = 0,
                startYear = 0,
                endYear = 0
            )
            viewModel.setChartTitle("All Time")
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}