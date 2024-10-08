package com.example.bancodigital.presenter.features.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.bancodigital.MainGraphDirections
import com.example.bancodigital.R
import com.example.bancodigital.data.enum.TransactionOperation
import com.example.bancodigital.data.enum.TransactionType
import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.databinding.FragmentDepositFormBinding
import com.example.bancodigital.util.MoneyTextWatcher
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.hideKeyboard
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheetValidateInputs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DepositFormFragment : Fragment() {

    private var _binding: FragmentDepositFormBinding? = null
    private val binding get() = _binding!!

    private val depositFormViewModel: DepositFormViewModel by viewModels()

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

    private fun initListeners() {
        binding.btnConfirm.setOnClickListener {
            validateDeposit()
        }

        with(binding.editAmount){
            addTextChangedListener(MoneyTextWatcher(this))

            doAfterTextChanged {
                this.text?.length?.let { this.setSelection(it) }
            }

        }
    }

    private fun validateDeposit() {
        hideKeyboard()
        val amount = MoneyTextWatcher.getValueUnMasked(binding.editAmount)

        if (amount <= 0f) {
            showBottomSheetValidateInputs(message = "Informe um valor para o deposito")
            return
        }

        val deposit = Deposit(amount = amount)
        saveDeposit(deposit)
    }

    private fun saveDeposit(deposit: Deposit) {
        depositFormViewModel.saveDeposit(deposit).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    saveTransaction(deposit)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                }
            }
        }
    }

    private fun saveTransaction(deposit: Deposit) {
        val transaction = Transaction(
            id = deposit.id,
            operation = TransactionOperation.DEPOSIT,
            date = deposit.date,
            amount = deposit.amount,
            type = TransactionType.CASH_IN
        )

        depositFormViewModel.saveTransaction(transaction).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {
                    val navOptions: NavOptions =
                        NavOptions.Builder().setPopUpTo(R.id.depositFormFragment, true).build()

                    val bundle = Bundle().apply {
                        putString("idDeposit", deposit.id)
                        putBoolean("showIconNavigation", false)
                    }

                    findNavController().navigate(R.id.action_global_depositReceiptFragment, bundle, navOptions)
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