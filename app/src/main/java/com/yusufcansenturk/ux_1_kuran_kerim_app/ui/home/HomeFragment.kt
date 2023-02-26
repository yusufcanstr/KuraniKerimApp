package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufcansenturk.ux_1_kuran_kerim_app.adapter.HomeAdapter
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.view.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeAdapter = HomeAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.loadSure()

        binding.recyclerViewList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewList.adapter = homeAdapter

        observeLiveData()

        editTextSearch()

        val randomHadis = viewModel.getRandomHadis()
        binding.txtHadis.text = randomHadis.hadis
        binding.txtHadisUser.text = randomHadis.name

    }

    private fun observeLiveData() {
        viewModel.sureList.observe(viewLifecycleOwner) { list ->
            list?.let {
                binding.loadingBar.visibility = View.GONE
                binding.recyclerViewList.visibility = View.VISIBLE
                binding.errorImg.visibility = View.GONE
                homeAdapter.updateNameList(list)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.errorImg.visibility = View.VISIBLE
                binding.loadingBar.visibility = View.GONE
                binding.recyclerViewList.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                if (it) {
                    binding.errorImg.visibility = View.GONE
                    binding.loadingBar.visibility = View.VISIBLE
                    binding.recyclerViewList.visibility = View.GONE
                }
            }
        }



    }

    private fun editTextSearch() {
        binding.searchEditText.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                println("before ->" + p0.toString())
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                println("onText ->" +p0.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                val newValue = s.toString()
                viewModel.searchSureList(newValue)
            }

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}