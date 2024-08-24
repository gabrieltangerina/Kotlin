package com.example.bancodigital.presenter.features.deposit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.R
import com.example.bancodigital.databinding.FragmentDepositReceiptBinding
import com.example.bancodigital.util.GetMask


class DepositReceiptFragment : Fragment() {

    private var _binding: FragmentDepositReceiptBinding? = null
    private val binding get() = _binding!!

    private val args: DepositReceiptFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDepositReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configData()
        initListeners()
    }

    private fun configData() {
        binding.textCodeTransaction.text = args.deposit.id

        binding.textDateTransaction.text = getString(
            R.string.text_balance_format_value,
            GetMask.getFormatedDate(args.deposit.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
        )


        binding.textAmountTransaction.text = GetMask.getFormatedValue(args.deposit.amount)
    }

    private fun initListeners() {
        binding.btnContinue.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}