package com.yusufcansenturk.ux_1_kuran_kerim_app.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.FragmentSignupBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.MainActivity
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.AuthResource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val view = binding.root




        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hesapOlusturText.setOnClickListener {
            // Signup Fragment -> Login Fragment
            val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
            findNavController().navigate(action)

        }

        binding.kayitOlButton.setOnClickListener {
            singupButtonFunction()
        }



    }

    private fun singupButtonFunction() {
        val name : String = binding.nameText.text.toString()
        val email : String = binding.emailText.text.toString()
        val password: String = binding.passwordText.text.toString()
        if (email.equals(null) || password.equals(null) || name.equals(null)) {
            Toast.makeText(requireContext(), "İsim , Email ve şifre giriniz !", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.signup(name, email, password)
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()


        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}