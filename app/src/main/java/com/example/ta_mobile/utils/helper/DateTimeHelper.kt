package com.example.ta_mobile.utils.helper

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object DateTimeHelper {

    @RequiresApi(Build.VERSION_CODES.O)
    fun time24hFormatter(hour:String, minute:String) : String{
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val time = LocalTime.parse("$hour:$minute", DateTimeFormatter.ofPattern("H:m"))
            .format(formatter)

        return time
    }

}