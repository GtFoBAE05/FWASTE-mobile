package com.example.ta_mobile.utils.extension

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.pranavpandey.android.dynamic.toasts.DynamicToast

fun Activity.showToast(text: String, longToast: Boolean = false) {
    if (longToast) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.showToast(text: String, longToast: Boolean = false) {
    activity?.let { context ->
        DynamicToast.make(context, text).show()
    }
}

fun Fragment.showSuccessToast(text: String, longToast: Boolean = false) {
    activity?.let { context ->
        DynamicToast.makeSuccess(context, text).show()
    }
}

fun Fragment.showErrorToast(text: String, longToast: Boolean = false) {
    activity?.let { context ->
        DynamicToast.makeError(context, text).show()
    }
}