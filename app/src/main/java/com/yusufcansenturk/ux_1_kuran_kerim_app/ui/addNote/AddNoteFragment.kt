package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.addNote

import android.os.Bundle
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

        navigationBarVisibility(view)

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

        }

        observeLiveData()

    }

    private fun observeLiveData() {
        viewmodel.noteList_LD.observe(viewLifecycleOwner) { notesLists ->
            notesLists?.let {
                binding.notesLists.adapter = NotesAdapter(it)
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

    private fun navigationBarVisibility(view: View) {
        val chipNavigationBar = view.findViewById<ChipNavigationBar>(R.id.navigation_bar)
        chipNavigationBar?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}