package com.example.ta_mobile.utils.helper

import java.text.NumberFormat
import java.util.Locale

object CurrencyHelper {
    fun convertToRupiah(price: Int): String {
        val formatter = NumberFormat.getNumberInstance(Locale("id", "ID"))
        val format = formatter as java.text.DecimalFormat
        format.applyPattern("Rp#,##0")
        return format.format(price)
    }

    fun convertToInt(currencyPrice: String): Int {
        val numericValue = currencyPrice.replace(Regex("[^0-9]"), "")

        if (numericValue.isNotEmpty()) {
            return numericValue.toInt()
        }

        return 0
    }
}