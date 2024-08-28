package com.example.bancodigital.presenter.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bancodigital.R
import com.example.bancodigital.databinding.FragmentTransferUserBinding
import com.example.bancodigital.presenter.features.transfer.adapter.TransferUserAdapter
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferUserFragment : Fragment() {

    private var _binding: FragmentTransferUserBinding? = null
    private val binding get() = _binding!!

    private val transferUserViewModel: TransferUserViewModel by viewModels()
    private lateinit var adapterTransferUser: TransferUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar, icon = R.drawable.ic_back_white)
        configRecyclerView()
        getProfiles()
    }

    private fun getProfiles(){
        transferUserViewModel.getProfilesUseCase().observe(viewLifecycleOwner){stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    adapterTransferUser.submitList(stateView.data)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun configRecyclerView() {
        adapterTransferUser = TransferUserAdapter { user ->
            Toast.makeText(requireContext(), "Nome: ${user.name}", Toast.LENGTH_SHORT).show()
        }

        with(binding.rvUsers) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterTransferUser
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}