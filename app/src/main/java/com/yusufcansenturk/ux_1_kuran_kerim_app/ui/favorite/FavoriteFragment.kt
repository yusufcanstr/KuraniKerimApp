package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.favorite

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yusufcansenturk.ux_1_kuran_kerim_app.adapter.FavoriteAdapter
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.FragmentFavoriteBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.model.favorite.Favorite
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() , OnFavoriteClickListener{

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var homeViewModel : HomeViewModel
    private val adapter = FavoriteAdapter(arrayListOf(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteList(requireContext())

        binding.recyclerLikeList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLikeList.adapter = adapter

        observeLiveData()

    }

    override fun onFavoriteClickListener(position: Int, favoriteList: ArrayList<Favorite>) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Kuran-ı Kerim")
        builder.setMessage("${favoriteList[position].name} Suresini favorilerden kaldırmak istediğinizden emin misiniz ?")
        builder.setPositiveButton("Evet") { dialog, _ ->
            homeViewModel.removeFavorite(favoriteList[position].id)
        }
        builder.setNegativeButton("Hayır") { dialog, _ -> }
        builder.create().show()
    }

    private fun observeLiveData() {
        viewModel.favoriteLists.observe(viewLifecycleOwner) { favoriteList->
            favoriteList?.let {
                binding.loadingBar.visibility = View.GONE
                binding.errorMessage.visibility = View.GONE
                binding.recyclerLikeList.visibility = View.VISIBLE
                adapter.updateNameList(it)
            }
        }

        viewModel.favoriteLoading.observe(viewLifecycleOwner) { loading ->
            loading?.let {
                binding.loadingBar.visibility = View.VISIBLE
                binding.errorMessage.visibility = View.GONE
                binding.recyclerLikeList.visibility = View.GONE
            }
        }

        viewModel.favoriteError.observe(viewLifecycleOwner) { error ->
            error?:let {
                binding.loadingBar.visibility = View.GONE
                binding.errorMessage.visibility = View.VISIBLE
                binding.recyclerLikeList.visibility = View.GONE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}