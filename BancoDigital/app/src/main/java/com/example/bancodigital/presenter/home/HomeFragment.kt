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
import com.example.bancodigital.data.enum.TransactionOperation
import com.example.bancodigital.data.enum.TransactionType
import com.example.bancodigital.data.model.Transaction
import com.example.bancodigital.data.model.User
import com.example.bancodigital.databinding.FragmentHomeBinding
import com.example.bancodigital.presenter.home.adapter.TransactionsAdapter
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.showBottomSheetValidateInputs
import com.example.bancodigital.util.showBottomSheetSignOut
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

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
        getProfile()
    }

    private fun configData(user: User){
        Picasso.get()
            .load(user.image)
            .fit()
            .centerCrop()
            .into(binding.userImage, object : Callback{
                override fun onSuccess() {
                    // Not yet implemented
                }

                override fun onError(e: Exception?) {
                    // Not yet implemented
                }

            })
    }

    private fun getProfile(){
        homeViewModel.getProfileUseCase().observe(viewLifecycleOwner){stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    stateView.data?.let { configData(it) }
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheetValidateInputs(message = stateView.message)
                }
            }
        }
    }

    private fun getTransactions() {
        homeViewModel.getTransactions().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    adapterTransaction.submitList(stateView.data?.reversed()?.take(6))
                    showBalance(stateView.data ?: emptyList())
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheetValidateInputs(message = stateView.message)
                }
            }
        }
    }

    private fun configRecyclerView() {
        adapterTransaction = TransactionsAdapter(requireContext()) { transaction ->
            when (transaction.operation) {
                TransactionOperation.DEPOSIT -> {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToDepositReceiptFragment(
                            transaction.id,
                            showIconNavigation = true
                        )

                    findNavController().navigate(action)
                }

                TransactionOperation.RECHARGE -> {
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToRechargeReceiptFragment(
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

    private fun initListeners() {
        binding.cardDeposit.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_depositFormFragment)
        }

        binding.btnShowAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
        }

        binding.cardExtract.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_extractFragment)
        }

        binding.btnLogout.setOnClickListener {
            showBottomSheetSignOut {
                FirebaseHelper.getAuth().signOut()
                findNavController().navigate(R.id.action_homeFragment_to_authentication)
            }
        }

        binding.cardProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.cardRechargePhone.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_rechargeFormFragment)
        }

        binding.cardTransfer.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_transferUserFragment)
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

        val total = cashIn - cashOut

        binding.textBalance.text =
            getString(R.string.text_balance_format_value, GetMask.getFormatedValue(total))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}