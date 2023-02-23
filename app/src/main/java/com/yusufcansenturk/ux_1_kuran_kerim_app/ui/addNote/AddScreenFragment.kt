package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.addNote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.yusufcansenturk.ux_1_kuran_kerim_app.R
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.FragmentAddScreenBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddScreenFragment : Fragment() {

    private var _binding: FragmentAddScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewmodel :AddNoteViewModel
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        viewmodel = ViewModelProvider(this)[AddNoteViewModel::class.java]

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kaytedButton.setOnClickListener {
            addMessage()
            val addNoteFragment = AddNoteFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame_Layout, addNoteFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        //navigationBarVisibility(view)

    }

    override fun onResume() {
        super.onResume()
        // Navigation bar'ı gizle
        (activity as MainActivity).hideNavigationBar()
    }

    override fun onStop() {
        super.onStop()
        // Navigation bar'ı geri göster
        (activity as MainActivity).showNavigationBar()
    }



    private fun addMessage() {
        val title = binding.titleText.text.toString()
        val note = binding.noteText.text.toString()
        val date = FieldValue.serverTimestamp()
        val email = auth.currentUser!!.email.toString()

        if ((title == "") && (note == "")) {
            Toast.makeText(requireContext(), "Başlık ve note alanını doldurunuz !", Toast.LENGTH_SHORT).show()
        }else {
            val notesHashMap = HashMap<String, Any>()
            notesHashMap.put("title", title)
            notesHashMap.put("note", note)
            notesHashMap.put("date", date)
            notesHashMap.put("email", email)

            viewmodel.addMessage(notesHashMap, requireContext())

        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}