package com.example.movieapp.presenter.auth.register

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
import com.example.movieapp.databinding.FragmentRegisterBinding
import com.example.movieapp.util.StateView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener { validateData() }

        Glide
            .with(requireContext())
            .load(R.drawable.loading)
            .into(binding.progressLoading);
    }

    private fun validateData() {
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString().trim()

        if (email.isEmpty()) Toast.makeText(requireContext(), "Email inválido", Toast.LENGTH_SHORT)
            .show()

        if (password.isEmpty()) Toast.makeText(
            requireContext(),
            "Senha inválida",
            Toast.LENGTH_SHORT
        ).show()

        registerUser(email, password)
    }

    private fun registerUser(email: String, password: String) {
        viewModel.register(email, password).observe(viewLifecycleOwner){stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.progressLoading.isVisible = true
                }

                is StateView.Success -> {
                    Toast.makeText(requireContext(), "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
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