package com.tkjen.youtube.utils

sealed class Result<out T> {
    data class Success<out R>(val data: R) : Result<R>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}