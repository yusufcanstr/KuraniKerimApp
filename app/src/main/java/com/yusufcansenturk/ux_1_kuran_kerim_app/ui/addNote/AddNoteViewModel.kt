package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.addNote

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.yusufcansenturk.ux_1_kuran_kerim_app.R
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
                Toast.makeText(context, R.string.not_eklendi, Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(context, R.string.internet_kontrolu, Toast.LENGTH_SHORT).show()
                    error_LD.value = true
                    loading_LD.value = false
                    noteList_LD.value = null
                }else {
                    if (value != null) {
                        if (value.isEmpty) {
                            loading_LD.value = false
                            Toast.makeText(context, R.string.not_yok, Toast.LENGTH_LONG).show()
                            error_LD.value = true
                        }else {
                            val documents = value.documents
                            val notesArrayList : ArrayList<Notes> = arrayListOf()
                            for (document in documents) {
                                val id = document.get("id") as String
                                val title = document.get("title") as String
                                val note = document.get("note") as String
                                val date = document.get("date")
                                val email = document.get("email") as String

                                val notes = Notes(id ,title, note, date, email)
                                notesArrayList.add(notes)
                            }

                            loading_LD.value = false
                            noteList_LD.value = notesArrayList

                        }
                    }
                }
            }
    }



    fun deleteData(documentId: String) {
        val itemsRef = db.collection(COLLECTION_NAME_NOTE)
        val query: Query = itemsRef.whereEqualTo("id", documentId)
        query.get().addOnCompleteListener(OnCompleteListener<QuerySnapshot?> { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    itemsRef.document(document.id).delete()
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.exception)
            }
        })
    }

    fun updateData(documentId: String, newData: Map<String, Any>) {
        db.collection(COLLECTION_NAME_NOTE)
            .whereEqualTo("id", documentId)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    // Only update the first matching document
                    val document = documents.first()
                    document.reference.update(newData)
                        .addOnSuccessListener { Log.d(TAG, "Data updated successfully") }
                        .addOnFailureListener { e -> Log.e(TAG, "Error updating data", e) }
                } else {
                    Log.d(TAG, "No document found with")
                }
            }
            .addOnFailureListener { e -> Log.e(TAG, "Error querying collection", e) }
    }


    fun getNoteById(noteId: String): LiveData<Notes?> {
        val notesRef = db.collection(COLLECTION_NAME_NOTE)
        val result = MutableLiveData<Notes?>()
        val query: Query = notesRef.whereEqualTo("id", noteId)
        query.addSnapshotListener { value, error ->
            if (value != null) {
                val documents = value.documents
                for (document in documents) {
                    val id = document.get("id") as String
                    val title = document.get("title") as String
                    val note = document.get("note") as String
                    val date = document.get("date")
                    val email = document.get("email") as String
                    val notes = Notes(id ,title, note, date, email)
                    result.postValue(notes)
                }
            }

            if (error != null) {
                Log.d(TAG, error.toString())
            }
        }
        return result
    }

}