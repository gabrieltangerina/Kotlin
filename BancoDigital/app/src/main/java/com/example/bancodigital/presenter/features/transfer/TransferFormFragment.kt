package com.example.bancodigital.presenter.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.R
import com.example.bancodigital.databinding.FragmentTransferFormBinding
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.MoneyTextWatcher
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.hideKeyboard
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheetValidateInputs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferFormFragment : Fragment() {

    private var _binding: FragmentTransferFormBinding? = null
    private val binding get() = _binding!!

    private val transferFormViewModel: TransferFormViewModel by viewModels()
    private val args: TransferFormFragmentArgs by navArgs()

    private var balance = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(toolbar = binding.toolbar, icon = R.drawable.ic_back_white)
        initListeners()
        getBalance()
    }

    private fun getBalance() {
        transferFormViewModel.getBalance().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.btnConfirm.isEnabled = false
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.btnConfirm.isEnabled = true
                    binding.progressBar.isVisible = false

                    stateView.data?.let {
                        balance = it
                    }

                    binding.textBalance.text = getString(
                        R.string.text_balance_format_value_form_fragment,
                        GetMask.getFormatedValue(balance)
                    )
                }

                is StateView.Error -> {
                    binding.btnConfirm.isEnabled = true
                    binding.progressBar.isVisible = false
                    showBottomSheetValidateInputs(message = stateView.message)
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnConfirm.setOnClickListener {
            validateDeposit()
        }

        with(binding.editAmount) {
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
            showBottomSheetValidateInputs(message = getString(R.string.text_message_empty_amount_transfer_form_fragment))
            return
        }

        if (amount > balance) {
            showBottomSheetValidateInputs(message = "Você não possui saldo para essa transferência")
            return
        }

        val action =
            TransferFormFragmentDirections.actionTransferFormFragmentToConfirmTransferFragment(
                user = args.user,
                amount = amount
            )

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}