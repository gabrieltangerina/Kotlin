package com.example.movieapp.presenter.auth.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentLoginBinding
import com.example.movieapp.databinding.FragmentRegisterBinding
import com.example.movieapp.util.StateView
import com.example.movieapp.util.hideKeyboard
import com.example.movieapp.util.isEmailValid
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

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

    private fun initListeners() {
        binding.btnLogin.setOnClickListener { validateData() }

        Glide
            .with(requireContext())
            .load(R.drawable.loading)
            .into(binding.progressLoading);
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        if (email.isEmailValid()) Toast.makeText(
            requireContext(),
            "Email inválido",
            Toast.LENGTH_SHORT
        ).show()

        if (password.isEmpty()) Toast.makeText(
            requireContext(),
            "Senha inválida",
            Toast.LENGTH_SHORT
        ).show()

        hideKeyboard()
        loginUser(email, password)
    }

    private fun loginUser(email: String, password: String) {
        viewModel.login(email, password).observe(viewLifecycleOwner){stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.progressLoading.isVisible = true
                }

                is StateView.Success -> {
                    Toast.makeText(requireContext(), "Login realizado com sucesso", Toast.LENGTH_SHORT).show()
                }

                is StateView.Error -> {
                    binding.progressLoading.isVisible = false
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}