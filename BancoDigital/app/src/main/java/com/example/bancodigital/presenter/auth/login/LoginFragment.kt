package com.example.bancodigital.presenter.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.bancodigital.R
import com.example.bancodigital.databinding.FragmentLoginBinding
import com.example.bancodigital.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners(){
        binding.btnLogin.setOnClickListener { validateData() }
    }

    private fun validateData(){
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        if(email.isEmpty()){
            Toast.makeText(requireContext(), "Informe um e-mail", Toast.LENGTH_SHORT).show()
            return
        }

        if(password.isEmpty()){
            Toast.makeText(requireContext(), "Informe uma senha", Toast.LENGTH_SHORT).show()
            return
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}