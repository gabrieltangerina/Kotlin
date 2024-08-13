package com.example.bancodigital.presenter.auth.recover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bancodigital.databinding.FragmentRecoverBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverFragment : Fragment() {

    private var _binding: FragmentRecoverBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners(){
        binding.btnRecover.setOnClickListener { validateData() }
    }

    private fun validateData(){
        val email = binding.editEmail.text.toString().trim()

        if(email.isNotEmpty()){

        }else{
            Toast.makeText(requireContext(), "Digite um e-mail", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}