package com.example.bancodigital.presenter.features.extract

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bancodigital.data.enum.TransactionOperation
import com.example.bancodigital.databinding.FragmentExtractBinding
import com.example.bancodigital.presenter.home.adapter.TransactionsAdapter
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheetValidateInputs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExtractFragment : Fragment() {

    private var _binding: FragmentExtractBinding? = null
    private val binding get() = _binding!!

    private val extractViewModel: ExtractViewModel by viewModels()
    private lateinit var adapterTransaction: TransactionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtractBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        configRecyclerView()
        getTransactions()
    }

    private fun configRecyclerView() {
        adapterTransaction = TransactionsAdapter(requireContext()) { transaction ->
            when (transaction.operation) {

                TransactionOperation.DEPOSIT -> {
                    val action =
                        ExtractFragmentDirections.actionExtractFragmentToDepositReceiptFragment(
                            transaction.id,
                            showIconNavigation = true
                        )

                    findNavController().navigate(action)
                }

                TransactionOperation.RECHARGE -> {
                    val action =
                        ExtractFragmentDirections.actionExtractFragmentToRechargeReceiptFragment(
                            transaction.id,
                            showIconNavigation = true
                        )

                    findNavController().navigate(action)
                }

                null -> {

                }
            }
        }

        with(binding.rvTransactions) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterTransaction
        }
    }

    private fun getTransactions() {
        extractViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    adapterTransaction.submitList(stateView.data?.reversed())
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheetValidateInputs(message = stateView.message)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}