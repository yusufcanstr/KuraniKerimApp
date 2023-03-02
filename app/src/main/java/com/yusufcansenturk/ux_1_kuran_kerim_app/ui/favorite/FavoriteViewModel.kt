package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.favorite

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.favorite.Favorite
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Constants.COLLECTION_FAVORITE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(

) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val email = auth.currentUser!!.email

    var favoriteLists = MutableLiveData<ArrayList<Favorite>>()
    var favoriteError = MutableLiveData<Boolean>()
    var favoriteLoading = MutableLiveData<Boolean>()


    fun getFavoriteList(context: Context) {
        viewModelScope.launch {
            favoriteLoading.value = true
            db.collection(COLLECTION_FAVORITE).document(email.toString())
                .collection("verses")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Toast.makeText(context, error.localizedMessage, Toast.LENGTH_SHORT).show()
                        favoriteError.value = true
                        favoriteLoading.value = false
                    }else{

                        if (value != null) {
                            if (value.isEmpty){
                                Toast.makeText(context, "Favori suren yok.", Toast.LENGTH_LONG).show()
                                favoriteError.value = true
                                favoriteLoading.value = false
                            }else {
                                val documents = value.documents
                                val favoriteArrayList : ArrayList<Favorite> = arrayListOf()
                                for (document in documents) {
                                    val favoriteId = document.get("id") as String
                                    val favoriteName = document.get("name") as String
                                    val favoriteBoolean = document.get("boolean") as Boolean

                                    val favorite = Favorite(favoriteId, favoriteName, favoriteBoolean)
                                    favoriteArrayList.add(favorite)

                                    favoriteLists.value = favoriteArrayList
                                }
                            }
                        }
                    }
                }
        }
    }

}