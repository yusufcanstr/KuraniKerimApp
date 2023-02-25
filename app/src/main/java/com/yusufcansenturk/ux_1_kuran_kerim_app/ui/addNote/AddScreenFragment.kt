package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.addNote

import android.os.Bundle
import android.text.Editable
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
import kotlin.properties.Delegates
import kotlin.random.Random

@AndroidEntryPoint
class AddScreenFragment : Fragment() {

    private var _binding: FragmentAddScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var addNoteViewModel :AddNoteViewModel
    private lateinit var auth : FirebaseAuth
    private var noteId by Delegates.notNull<String>()

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

        addNoteViewModel = ViewModelProvider(this)[AddNoteViewModel::class.java]

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myInt = arguments?.getInt("myInt")
        val id = arguments?.getString("id")
        if (id != null) {
            noteId = id
        }
        println(myInt)


        if (myInt == 0) {
            //Not Ekle
            addButton()
        }else if (myInt == 1) {
            //Notu güncelle
            getNote(noteId)
            updateButton(noteId)
        }




    }


    private fun addMessage() {
        val title = binding.titleText.text.toString()
        val note = binding.noteText.text.toString()
        val date = FieldValue.serverTimestamp()
        val email = auth.currentUser!!.email.toString()
        val randomInt = (0..99999999999).random().toString()

        if ((title == "") && (note == "")) {
            Toast.makeText(requireContext(), "Başlık ve note alanını doldurunuz !", Toast.LENGTH_SHORT).show()
        }else {
            val notesHashMap = HashMap<String, Any>()
            notesHashMap.put("id", randomInt)
            notesHashMap.put("title", title)
            notesHashMap.put("note", note)
            notesHashMap.put("date", date)
            notesHashMap.put("email", email)

            addNoteViewModel.addMessage(notesHashMap, requireContext())

        }


    }

    private fun updateButton(docId :String) {
        binding.kaytedButton.setOnClickListener {
            noteUpdates(docId)
            val addNoteFragment = AddNoteFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame_Layout, addNoteFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private fun noteUpdates(docId :String) {

            val title = binding.titleText.text.toString()
            val note = binding.noteText.text.toString()
            val date = FieldValue.serverTimestamp()
            val email = auth.currentUser!!.email.toString()

            if ((title == "") && (note == "")) {
                Toast.makeText(requireContext(), "Başlık ve note alanını doldurunuz !", Toast.LENGTH_SHORT).show()
            }else {
                val notesHashMap = HashMap<String, Any>()
                notesHashMap.put("id", noteId)
                notesHashMap.put("title", title)
                notesHashMap.put("note", note)
                notesHashMap.put("date", date)
                notesHashMap.put("email", email)

                addNoteViewModel.updateData(docId, notesHashMap)

            }




    }

    fun addButton() {
        binding.kaytedButton.setOnClickListener {
            addMessage()
            val addNoteFragment = AddNoteFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame_Layout, addNoteFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    fun getNote(docId: String) {
        addNoteViewModel.getNoteById(docId).observe(viewLifecycleOwner) { note ->
            if (note != null) {
                binding.titleText.text = note.title?.toEditable()
                binding.noteText.text = note.note?.toEditable()
            }else {
                Toast.makeText(requireContext() , "Belge alınamadı", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}