package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.settings

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.Constants.COLLECTION_NAME_USERS
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.providesDownloadImage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(

) :ViewModel() {


    private val _profileImage = MutableLiveData<Bitmap?>()
    var profileImage: MutableLiveData<Bitmap?> = _profileImage

    private var auth: FirebaseAuth = Firebase.auth
    val db = FirebaseFirestore.getInstance()
    private val currentUser = auth.currentUser
    private val email = currentUser!!.email

    fun setProfileImage(image: Bitmap?) {
        _profileImage.value = image
    }

    fun downloadProfileImage(image: Bitmap, context: Context) {
        val storageReference = FirebaseStorage.getInstance().reference
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()

        val uploadTask = storageReference.child("ProfileUrl/$email/${email}.jpg").putBytes(data)
        uploadTask.addOnSuccessListener {

            setProfileImage(image)
            Toast.makeText(context, "Profil fotoğrafı eklendi", Toast.LENGTH_LONG).show()

        }.addOnFailureListener {
            Toast.makeText(context, "Bir hata oluştu.", Toast.LENGTH_LONG).show()
            println(it.localizedMessage)
        }
    }

    fun updateProfileImage(image: Bitmap, context: Context) {
        val storageReference = FirebaseStorage.getInstance().reference
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()

        val uploadTask = storageReference.child("ProfileUrl/$email/${email}.jpg").putBytes(data)
        uploadTask.addOnSuccessListener {
            currentUser?.updateProfile(
                UserProfileChangeRequest.Builder().setPhotoUri(it.uploadSessionUri).build()
            )?.addOnSuccessListener {

                setProfileImage(image)
                Toast.makeText(context, "Profil resmi güncellendi", Toast.LENGTH_LONG).show()

            }?.addOnFailureListener {
                Toast.makeText(context, "Bir hata oluştu", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Bir hata oluştu", Toast.LENGTH_LONG).show()
        }
    }



    fun getProfileImageView(context: Context, imageView: ImageView) {
        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("ProfileUrl/$email/${email}.jpg")

        imageRef.downloadUrl.addOnSuccessListener {
            providesDownloadImage(context, imageView, it.toString())
        }.addOnFailureListener {
            println(it.localizedMessage)
        }
    }


    fun firestoreUpdateImageURL( context: Context) {
        val userRef = db.collection(COLLECTION_NAME_USERS).document(email.toString())


        val storageRef = FirebaseStorage.getInstance().reference.child("ProfileUrl/$email/${email}.jpg")
        storageRef.downloadUrl.addOnSuccessListener {
            println(it)

            userRef.update("profileUrl", it.toString())

                .addOnSuccessListener {
                    Toast.makeText(context, "Profile URL updated", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error updating profile URL", Toast.LENGTH_LONG).show()
                    it.printStackTrace()
                }

        }.addOnFailureListener {
            it.printStackTrace()
        }

    }

    fun getCurrentName(userName:String, callback: (String) -> Unit) {

        db.collection(COLLECTION_NAME_USERS).document(userName)
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null) {
                    val name = doc.getString("Name").toString()
                    callback(name)
                }
            }
            .addOnFailureListener {

            }
    }





}