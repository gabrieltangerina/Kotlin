package com.example.bancodigital.presenter.features.recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.R
import com.example.bancodigital.data.model.Recharge
import com.example.bancodigital.databinding.FragmentRechargeRecepitBinding
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RechargeReceiptFragment : Fragment() {

    private var _binding: FragmentRechargeRecepitBinding? = null
    private val binding get() = _binding!!

    private val rechargeReceiptViewModel: RechargeReceiptViewModel by viewModels()
    private val args: RechargeReceiptFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRechargeRecepitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar, showIconNavigation = args.showIconNavigation)
        getRecharge()
        initListeners()
    }

    private fun getRecharge() {
        rechargeReceiptViewModel.getRecharge(args.idRecharge)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {

                    }

                    is StateView.Success -> {
                        stateView.data?.let {
                            configData(it)
                        }
                    }

                    is StateView.Error -> {
                        Toast.makeText(requireContext(), "Ocorreu um erro", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                    }
                }
            }
    }

    private fun configData(recharge: Recharge) {
        binding.textCodeTransaction.text = recharge.id

        binding.textDateTransaction.text = getString(
            R.string.text_balance_format_value,
            GetMask.getFormatedDate(recharge.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
        )

        binding.textPhone.text = recharge.number

        binding.textAmountTransaction.text = GetMask.getFormatedValue(recharge.amount)
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