package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.addNote

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.Notes.Notes
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Constants.COLLECTION_NAME_NOTE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(

) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val noteList_LD = MutableLiveData<List<Notes>?>()
    val error_LD = MutableLiveData<Boolean>()
    val loading_LD = MutableLiveData<Boolean>()


    fun addMessage(noteList : HashMap<String, Any>, context: Context) {
        db.collection(COLLECTION_NAME_NOTE).add(noteList)
            .addOnSuccessListener {
                Toast.makeText(context, "Not başarılı bir şekilde eklendi.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                println(it.localizedMessage)
            }

    }

    fun getMessage(context: Context) {
        loading_LD.value = true

        db.collection(COLLECTION_NAME_NOTE)
            //.orderBy("date", Query.Direction.DESCENDING)
            .whereEqualTo("email", auth.currentUser!!.email.toString())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    println(error.localizedMessage)
                    Toast.makeText(context, "İnternet bağlantınızı kontrol ediniz !", Toast.LENGTH_SHORT).show()
                    error_LD.value = true
                    loading_LD.value = false
                    noteList_LD.value = null
                }else {
                    if (value != null) {
                        if (value.isEmpty) {
                            Toast.makeText(context, "Notlar yok !", Toast.LENGTH_LONG).show()
                            error_LD.value = true
                            loading_LD.value = false
                        }else {
                            val documents = value.documents
                            val notesArrayList : ArrayList<Notes> = arrayListOf()
                            for (document in documents) {
                                val title = document.get("title") as String
                                val note = document.get("note") as String
                                val date = document.get("date")
                                val email = document.get("email") as String

                                val notes = Notes(title, note, date, email)
                                notesArrayList.add(notes)
                            }

                            loading_LD.value = false
                            noteList_LD.value = notesArrayList

                        }
                    }
                }
            }
    }

}