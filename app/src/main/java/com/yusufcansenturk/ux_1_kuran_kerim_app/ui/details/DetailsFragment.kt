package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.details

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufcansenturk.ux_1_kuran_kerim_app.adapter.DetailsAdapter
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.FragmentDetailsBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val viewModel by viewModels<DetailsViewModel>()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sureId = requireActivity().intent.getStringExtra("sure_id").toString()
        val name = requireActivity().intent.getStringExtra("name").toString()
        binding.itemDetailName.text = name

        binding.recyclerDetailSureList.layoutManager = LinearLayoutManager(requireContext())

        backButton()

        sureId?.let {
            viewModel.getData(it, requireContext())
            observeLiveData()
        }
    }

    private fun observeLiveData() {
        viewModel.sureList.observe(viewLifecycleOwner) { sureList ->
            binding.recyclerDetailSureList.adapter = DetailsAdapter(sureList)
            binding.errorMessage.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
        }
    }

    fun backButton() {
        binding.backButton.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}