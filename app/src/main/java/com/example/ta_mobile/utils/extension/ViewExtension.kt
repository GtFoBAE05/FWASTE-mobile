package com.example.ta_mobile.utils.extension

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.invisible(){
    this.visibility = View.INVISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

fun NavController.safeNavigateWithArgs(direction: NavDirections, bundle: Bundle?) {
    currentDestination?.getAction(direction.actionId)?.run {
        navigate(direction.actionId, bundle)
    }
}