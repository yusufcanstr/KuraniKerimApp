package com.yusufcansenturk.ux_1_kuran_kerim_app.util

import java.lang.Exception

sealed class AuthResource<out R> {
    data class Success<out R>(val result: R) : AuthResource<R>()
    data class Failure(val exception: Exception) : AuthResource<Nothing>()
    object Loading : AuthResource<Nothing>()
}