package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufcansenturk.ux_1_kuran_kerim_app.adapter.HomeAdapter
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeAdapter = HomeAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

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

        //viewModel.loadSure()

        binding.recyclerViewList.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewList.adapter = homeAdapter

        observeLiveData()


    }

    private fun observeLiveData() {
        viewModel.sureList.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                binding.loadingBar.visibility = View.GONE
                binding.recyclerViewList.visibility = View.VISIBLE
                binding.errorImg.visibility = View.GONE
                homeAdapter.updateCountryList(list)
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                binding.errorImg.visibility = View.VISIBLE
                binding.loadingBar.visibility = View.GONE
                binding.recyclerViewList.visibility = View.GONE
                println("hatageliyor")
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                binding.recyclerViewList.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}