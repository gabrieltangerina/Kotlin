package com.example.bancodigital.presenter.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.example.bancodigital.R
import com.example.bancodigital.databinding.FragmentTransferFormBinding
import com.example.bancodigital.util.MoneyTextWatcher
import com.example.bancodigital.util.hideKeyboard
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheetValidateInputs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferFormFragment : Fragment() {

    private var _binding: FragmentTransferFormBinding? = null
    private val binding get() = _binding!!

    // private val depositFormViewModel: DepositFormViewModel by viewModels()

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

        // Navegação para a tela de confirmação da transferência
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}