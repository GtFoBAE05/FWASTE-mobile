package com.example.ta_mobile.utils

sealed class NetworkResult<out R> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val error: String) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}