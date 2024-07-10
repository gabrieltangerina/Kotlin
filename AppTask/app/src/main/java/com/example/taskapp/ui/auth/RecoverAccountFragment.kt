package com.example.taskapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.taskapp.R
import com.example.taskapp.databinding.FragmentRecoverAccountBinding
import com.example.taskapp.ui.BaseFragment
import com.example.taskapp.util.FirebaseHelper
import com.example.taskapp.util.initToolbar
import com.example.taskapp.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RecoverAccountFragment : BaseFragment() {

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

        initToolbar(binding.toolbar)
        initListener()
    }

    private fun initListener(){
        binding.btnRecover.setOnClickListener{
            validateData()
        }
    }

    private fun validateData(){
        val email = binding.editEmail.text.toString().trim()

        if(email.isNotEmpty()){
            hideKeyboard()
            binding.toolbar.isVisible = true
            recoverAccountUser(email)
        }else{
            showBottomSheet(message = getString(R.string.email_empty_warning_login))
        }
    }

    private fun recoverAccountUser(email: String){
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            binding.progressBar.isVisible = false
            if(task.isSuccessful){
                showBottomSheet(
                    message = getString(R.string.text_message_recover_account_fragment)
                )
            }else{
                showBottomSheet(
                    message = getString(FirebaseHelper.validError(task.exception?.message.toString()))
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}