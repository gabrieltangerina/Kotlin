package com.example.bancodigital.presenter.features.recharge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bancodigital.R
import com.example.bancodigital.databinding.FragmentDepositReceiptBinding
import com.example.bancodigital.databinding.FragmentRechargeFormBinding
import com.example.bancodigital.util.hideKeyboard
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet

class RechargeFormFragment : Fragment() {

    private var _binding: FragmentRechargeFormBinding? = null
    private val binding get() = _binding!!

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

    private fun initListeners(){
        binding.btnConfirm.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        hideKeyboard()
        val amount = binding.editAmount.text.toString().trim()
        val phone = binding.editPhone.unMaskedText.toString()

        if (amount.isEmpty()) {
            showBottomSheet(message = getString(R.string.text_amount_empty))
            return
        }

        if(phone.isEmpty()){
            showBottomSheet(message = getString(R.string.text_phone_empty))
            return
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}