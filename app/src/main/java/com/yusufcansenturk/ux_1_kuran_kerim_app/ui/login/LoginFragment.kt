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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.yusufcansenturk.ux_1_kuran_kerim_app.R
import com.yusufcansenturk.ux_1_kuran_kerim_app.databinding.FragmentLoginBinding
import com.yusufcansenturk.ux_1_kuran_kerim_app.ui.MainActivity
import com.yusufcansenturk.ux_1_kuran_kerim_app.util.AuthResource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel:LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]


        viewModel.viewModelScope.launch {
            viewModel.loginFlow.collect { resource ->
                when(resource) {
                    is AuthResource.Failure -> "Error Mesajı"
                    AuthResource.Loading -> "Yükleniyor mesajı"
                    is AuthResource.Success -> {
                        //Login Fragment -> Main Activity
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()


                    }
                    else -> {}
                }
            }
        }





    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hesapOlusturText.setOnClickListener {
            //Login Frg -> Signup Frg
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            findNavController().navigate(action)
        }

        binding.kayitOlText.setOnClickListener {
            loginButtonFunction()
        }

    }

    private fun loginButtonFunction() {
        val email : String = binding.emailText.text.toString()
        val password: String = binding.passwordText.text.toString()
        if (email.equals(null) || password.equals(null)) {
            Toast.makeText(requireContext(), "Email ve şifre giriniz !", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.login(email, password)

        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}