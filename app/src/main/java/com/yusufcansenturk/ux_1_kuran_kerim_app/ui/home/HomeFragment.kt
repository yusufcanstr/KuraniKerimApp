package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufcansenturk.ux_1_kuran_kerim_app.adapter.HomeAdapter
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.FragmentHomeBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.SurelerList.Data
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.favorite.Favorite
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.favorite.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnLikeButtonClickListener {

    private lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeAdapter = HomeAdapter(arrayListOf(), this)

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
                homeAdapter.updateNameList(it)
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

    var sayac = 0
    override fun onLikeButtonClicked(
        position: Int,
        sureList: List<Data>,
        img1: ImageView,
        img2: ImageView,
    ) {
        val id = sureList[position].id.toString()
        val name = sureList[position].name!!

        sayac++
        val sonuc = if (sayac % 2 == 1){
            //1
            addFavorite(id, name, true, id)
            img1.visibility = View.INVISIBLE
            img2.visibility = View.VISIBLE
        } else {
            //0
            removeFavorite(id)
            img2.visibility = View.INVISIBLE
            img1.visibility = View.VISIBLE
        }
    }


    private fun addFavorite(sureId: String, sureName :String, boolean: Boolean, id: String) {

        val sureHashMap = HashMap<String, Any>()
        sureHashMap.put("id", sureId)
        sureHashMap.put("name", sureName)
        sureHashMap.put("boolean", boolean)

        viewModel.addFavorite(sureHashMap , id)
    }

    private fun removeFavorite(sureId: String) {
        viewModel.removeFavorite(sureId!!)
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