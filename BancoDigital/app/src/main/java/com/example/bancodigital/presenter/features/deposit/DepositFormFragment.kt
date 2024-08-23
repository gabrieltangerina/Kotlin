package com.example.bancodigital.presenter.features.deposit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.bancodigital.R
import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.databinding.FragmentDepositFormBinding
import com.example.bancodigital.databinding.FragmentHomeBinding
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositFormFragment : Fragment() {

    private var _binding: FragmentDepositFormBinding? = null
    private val binding get() = _binding!!

    private val depositViewModel: DepositViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepositFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(toolbar = binding.toolbar, icon = R.drawable.ic_back_white)
        initListeners()
    }

    private fun initListeners(){
        binding.btnConfirm.setOnClickListener {
            validateDeposit()
        }
    }

    private fun validateDeposit(){
        val amount = binding.editAmount.text.toString().trim()

        if(amount.isEmpty()){
            Toast.makeText(requireContext(), "Informe um valor para o deposito", Toast.LENGTH_SHORT).show()
            return
        }

        val deposit = Deposit(amount = amount.toFloat())
        saveDeposit(deposit)
    }

    private fun saveDeposit(deposit: Deposit){
        depositViewModel.saveDeposit(deposit).observe(viewLifecycleOwner){stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }
                
                is StateView.Sucess -> {
                    Toast.makeText(requireContext(), "Deposito salvo", Toast.LENGTH_SHORT).show()
                }
                
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}