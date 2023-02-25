package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.addNote

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.yusufcansenturk.ux_1_kuran_kerim_app.R
import com.yusufcansenturk.ux_1_kuran_kerim_app.adapter.NotesAdapter
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.FragmentAddNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewmodel :AddNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel = ViewModelProvider(this)[AddNoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notesLists.layoutManager = GridLayoutManager(requireContext(), 2)
        viewmodel.getMessage(requireContext())

        binding.addButton.setOnClickListener {

            val targetFragment = AddScreenFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.frame_Layout, targetFragment)
                ?.addToBackStack(null)
                ?.commit()

            targetFragment.arguments = Bundle().apply {
                putInt("myInt", 0)
            }

        }

        observeLiveData()


    }

    private fun setupRecyclerViewTouchListener(adapter: NotesAdapter) {
        val recyclerView = binding.notesLists

        recyclerView.addOnItemTouchListener(MyLongClickListener(
            onItemClick = { position ->
                // Öğeye tıklandığında yapılacak işlemler
                val item = adapter.getItem(position)
                val docId = item.id

                val targetFragment = AddScreenFragment()
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.frame_Layout, targetFragment)
                    ?.addToBackStack(null)
                    ?.commit()

                targetFragment.arguments = Bundle().apply {
                    putInt("myInt", 1)
                    putString("id", docId)
                }

            },
            onLongItemClick = { position ->
                // Öğeye uzun tıklandığında yapılacak işlemler
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage(R.string.not_soru)
                builder.setPositiveButton("Evet") { dialog, which ->
                    val item = adapter.getItem(position)
                    val docId = item.id
                    viewmodel.deleteData(docId)
                }

                builder.setNegativeButton("Hayır") { dialog, which ->

                }
                builder.show()

            },
            recyclerView = recyclerView
        ))
    }

    private fun observeLiveData() {
        viewmodel.noteList_LD.observe(viewLifecycleOwner) { notesLists ->
            notesLists?.let {
                binding.notesLists.adapter = NotesAdapter(it)
                setupRecyclerViewTouchListener(NotesAdapter(it))
                binding.errorImage.visibility = View.GONE
                binding.progressBar2.visibility = View.GONE
            }
        }

        viewmodel.loading_LD.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                binding.errorImage.visibility = View.GONE
                binding.progressBar2.visibility = View.VISIBLE
            }
        }

        viewmodel.error_LD.observe(viewLifecycleOwner) {error ->
            error?.let {
                binding.errorImage.visibility = View.VISIBLE
                binding.progressBar2.visibility = View.GONE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}