package com.example.ta_mobile.utils.extension

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Activity.showToast(text: String, longToast: Boolean = false) {
    if (longToast) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.showToast(text: String, longToast: Boolean = false) {
    activity?.let { context ->
        if (longToast) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}