package com.yusufcansenturk.ux_1_kuran_kerim_app.repository

import com.google.firebase.auth.FirebaseUser
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.AuthResource


interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email:String, password:String) : AuthResource<FirebaseUser>
    suspend fun signup(name:String, email:String, password: String): AuthResource<FirebaseUser>
    fun logout()
}

