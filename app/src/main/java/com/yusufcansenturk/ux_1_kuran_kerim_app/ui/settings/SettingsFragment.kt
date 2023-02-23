package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.FragmentSettingsBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.login.LoginActivity
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel
    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var auth: FirebaseAuth
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedBitmap :Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        registerLauncher()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root

        val currentUserEmail = auth.currentUser!!.email
        binding.userEmail.text = currentUserEmail
        settingsViewModel.getCurrentName(currentUserEmail!!) {
            binding.userName.text = it
        }

        val profileImageView = binding.profileImage
        settingsViewModel.getProfileImageView(requireContext(),profileImageView)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.signOut.setOnClickListener {
            onAlertDialog(it)
        }

        binding.profileImage.setOnClickListener {
            chooseImage(it)
        }


    }

    private fun chooseImage(view: View) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(view, "Profil fotoğrafı yüklemek için galeri iznine ihtiyacım var ?", Snackbar.LENGTH_INDEFINITE)
                    .setAction("İzin ver", View.OnClickListener {
                        //request permission
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }).show()
            }else {
                // request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else {
            val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }

    private fun registerLauncher() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val intentFromResult = result.data
                if (intentFromResult != null) {
                    val imageData = intentFromResult.data

                    if (imageData != null) {
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                val source = ImageDecoder.createSource(requireActivity().contentResolver, imageData)
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.profileImage.setImageBitmap(selectedBitmap)
                                profileImageDownload(selectedBitmap!!)
                            }else {
                                selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageData)
                                binding.profileImage.setImageBitmap(selectedBitmap)
                                profileImageDownload(selectedBitmap!!)
                            }
                        }catch (e:Exception) {
                            e.printStackTrace()
                        }
                    }


                }
            }
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {result ->
            if (result) {
                // permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)

            }else {
                Toast.makeText(requireContext(), "Galeriye gitmek için izn lazım !", Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun makeSmallerBitmap(image: Bitmap, maximumSize: Int) : Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio : Double = width.toDouble() / height.toDouble()

        if (bitmapRatio > 0){
            width = maximumSize
            val scaledHeight = width / bitmapRatio
            height = scaledHeight.toInt()
        }else {
            height = maximumSize
            val scaleWidth = height * bitmapRatio
            width = scaleWidth.toInt()
        }

        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun profileImageDownload(selectedBitmap: Bitmap) {
        val smallBitmap = makeSmallerBitmap(selectedBitmap, 200)
        settingsViewModel.setProfileImage(smallBitmap)


        val profilImage = settingsViewModel.profileImage.value
        if (profilImage != null) {
            settingsViewModel.updateProfileImage(smallBitmap, requireContext())
            settingsViewModel.firestoreUpdateImageURL(requireContext())
        }else {
            settingsViewModel.downloadProfileImage(smallBitmap, requireContext())
            settingsViewModel.firestoreUpdateImageURL(requireContext())
        }

    }

    fun onAlertDialog(view: View) {
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Chat App")
        builder.setMessage("Uygulamadan çıkmak istediğinizden emin misiniz ?")
        builder.setPositiveButton("Evet") { dialog, id ->
            viewModel.logout()
            settingsViewModel.setProfileImage(null)
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        builder.setNegativeButton("Hayır") { dialog, id -> }
        builder.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}