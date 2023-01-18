package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.FragmentSettingsBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.login.LoginActivity
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.login.LoginFragmentDirections
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.signOut.setOnClickListener {
            viewModel.logout()
            //MainActivity -> LoginActivity -> Finish()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            activity?.finish()
        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}