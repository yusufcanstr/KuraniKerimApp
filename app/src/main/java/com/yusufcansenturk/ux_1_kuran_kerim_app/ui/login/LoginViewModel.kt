package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.yusufcansenturk.ux_1_kuran_kerim_app.repository.AuthRepository
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.AuthResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<AuthResource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<AuthResource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<AuthResource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<AuthResource<FirebaseUser>?> = _loginFlow

    val currentUser : FirebaseUser?
        get() = repository.currentUser

    init {
        if (repository.currentUser != null) {
            _loginFlow.value = AuthResource.Success(repository.currentUser!!)
        }
    }

    fun login(email:String, password:String) = viewModelScope.launch {
        _loginFlow.value = AuthResource.Loading
        val result = repository.login(email, password)
        _loginFlow.value = result
    }

    fun signup(name:String, email:String, password:String) = viewModelScope.launch {
        _signupFlow.value = AuthResource.Loading
        val result = repository.signup(name, email, password)
        _signupFlow.value = result
    }

    fun logout() {
        repository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }




}