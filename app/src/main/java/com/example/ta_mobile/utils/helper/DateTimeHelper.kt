package com.example.ta_mobile.utils.helper

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeHelper {

    @RequiresApi(Build.VERSION_CODES.O)
    fun time24hFormatter(hour:String, minute:String) : String{
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val time = LocalTime.parse("$hour:$minute", DateTimeFormatter.ofPattern("H:m"))
            .format(formatter)

        return time
    }

    fun convertDate(dateStr: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateStr)
        return date?.let { outputFormat.format(it) } ?: ""
    }

}