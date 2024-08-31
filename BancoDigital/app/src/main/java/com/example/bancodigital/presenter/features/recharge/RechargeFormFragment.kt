package com.example.bancodigital.presenter.features.recharge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bancodigital.R
import com.example.bancodigital.data.enum.TransactionOperation
import com.example.bancodigital.data.enum.TransactionType
import com.example.bancodigital.data.model.Recharge
import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.databinding.FragmentRechargeFormBinding
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.hideKeyboard
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheetValidateInputs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeFormFragment : Fragment() {

    private var _binding: FragmentRechargeFormBinding? = null
    private val binding get() = _binding!!

    private val rechargeViewModel: RechargeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRechargeFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar, icon = R.drawable.ic_back_white)
        initListeners()
    }

    private fun saveRecharge(recharge: Recharge) {
        rechargeViewModel.saveRecharge(recharge).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    saveTransaction(recharge)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                }
            }
        }
    }

    private fun saveTransaction(recharge: Recharge) {
        val transaction = Transaction(
            id = recharge.id,
            operation = TransactionOperation.RECHARGE,
            date = recharge.date,
            amount = recharge.amount,
            type = TransactionType.CASH_OUT
        )

        rechargeViewModel.saveTransaction(transaction).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {
                    val action = RechargeFormFragmentDirections
                        .actionRechargeFormFragmentToRechargeReceiptFragment(
                            recharge.id,
                            showIconNavigation = false
                        )

                    findNavController().navigate(action)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                }
            }
        }
    }

    private fun initListeners(){
        binding.btnConfirm.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        hideKeyboard()
        binding.progressBar.isVisible = true

        val amount = binding.editAmount.text.toString().trim()
        val phone = binding.editPhone.unMaskedText.toString()

        if (amount.isEmpty()) {
            showBottomSheetValidateInputs(message = getString(R.string.text_amount_empty))
            return
        }

        if(phone.isEmpty()){
            showBottomSheetValidateInputs(message = getString(R.string.text_phone_empty))
            return
        }

        val recharge = Recharge(
            amount = amount.toFloat(),
            number = phone
        )

        saveRecharge(recharge)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}