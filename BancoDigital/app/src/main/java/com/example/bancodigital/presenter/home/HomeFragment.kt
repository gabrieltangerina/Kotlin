package com.example.bancodigital.presenter.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bancodigital.R
import com.example.bancodigital.data.enum.TransactionType
import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.databinding.FragmentHomeBinding
import com.example.bancodigital.presenter.home.adapter.TransactionsAdapter
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var adapterTransaction: TransactionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configRecyclerView()
        getTransactions()
        initListeners()
    }

    private fun getTransactions() {
        homeViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Sucess -> {
                    binding.progressBar.isVisible = false
                    adapterTransaction.submitList(stateView.data?.reversed()?.take(6))
                    showBalance(stateView.data ?: emptyList())
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun configRecyclerView() {
        adapterTransaction = TransactionsAdapter(requireContext()) { transaction ->

        }

        with(binding.rvTransactions) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterTransaction
        }
    }

    private fun initListeners() {
        binding.cardDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_depositFormFragment)
        }
    }

    private fun showBalance(transactions: List<Transaction>) {
        var cashIn = 0f
        var cashOut = 0f

        transactions.forEach { transaction ->
            if (transaction.type == TransactionType.CASH_IN) {
                cashIn += transaction.amount
            } else {
                cashOut += transaction.amount
            }
        }

        var total = cashIn - cashOut

        binding.textBalance.text =
            getString(R.string.text_balance_format_value, GetMask.getFormatedValue(total))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}