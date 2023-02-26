package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.login


import android.app.Application
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.yusufcansenturk.ux_1_kuran_kerim_app.ApplicationApp
import com.yusufcansenturk.ux_1_kuran_kerim_app.R
import com.yusufcansenturk.ux_1_kuran_kerim_app.repository.AuthRepository
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.AuthResource
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Constants.COLLECTION_NAME_USERS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _loginFlow = MutableStateFlow<AuthResource<FirebaseUser>?>(null)
    val loginFlow: StateFlow<AuthResource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<AuthResource<FirebaseUser>?>(null)
    val signupFlow: StateFlow<AuthResource<FirebaseUser>?> = _loginFlow


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

    fun saveFirestoreDatabase(usersList: HashMap<String, Any>, UsersId: String) {
        db.collection(COLLECTION_NAME_USERS).document(UsersId).set(usersList)
            .addOnSuccessListener {
                println("Kişi veritabanına eklendi")
            }.addOnFailureListener {
                println("hata mesajı ->" + it.localizedMessage)
            }
    }


}