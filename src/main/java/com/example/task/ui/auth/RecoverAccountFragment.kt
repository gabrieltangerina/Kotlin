package com.example.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.task.R
import com.example.task.databinding.FragmentLoginBinding
import com.example.task.databinding.FragmentRecoverAccountBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class RecoverAccountFragment : Fragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        initClicks()
    }

    private fun initClicks(){
        binding.btnSend.setOnClickListener { validateData() }
    }

    private fun validateData(){
        val email = binding.edtEmail.text.toString().trim()

        if (email.isNotEmpty()){

            binding.progressBar.isVisible = true
            recoverAccountUser(email)

        }else{
            Toast.makeText(requireContext(), "Informe seu e-mail", Toast.LENGTH_SHORT).show()
        }
    }

    private fun recoverAccountUser(email:String){
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Pronto, acabamos de enviar um link para o seu e-mail.", Toast.LENGTH_SHORT).show()
                }

                binding.progressBar.isVisible = false
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}