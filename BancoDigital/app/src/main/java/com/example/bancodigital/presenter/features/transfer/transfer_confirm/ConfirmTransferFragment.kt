package com.example.bancodigital.presenter.features.transfer.transfer_confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.R
import com.example.bancodigital.data.model.Transfer
import com.example.bancodigital.databinding.FragmentTransferConfirmBinding
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheetValidateInputs
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmTransferFragment : Fragment() {

    private var _binding: FragmentTransferConfirmBinding? = null
    private val binding get() = _binding!!

    private val args: ConfirmTransferFragmentArgs by navArgs()
    private val confirmTransferViewModel: ConfirmTransferViewModel by viewModels()

    private val tagPicasso = "tagPicasso"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(toolbar = binding.toolbar)
        configData()
        initListeners()
    }

    private fun configData() {

        if (args.user.image != "") {
            Picasso.get()
                .load(args.user.image)
                .tag(tagPicasso)
                .fit()
                .centerCrop()
                .into(binding.imageProfile, object : Callback {
                    override fun onSuccess() {
                        // TODO("Not yet implemented")
                    }

                    override fun onError(e: Exception?) {
                        // TODO("Not yet implemented")
                    }

                })
        }

        binding.textUsername.text = args.user.name

        binding.textAmountTransaction.text = getString(
            R.string.text_balance_format_value,
            GetMask.getFormatedValue(args.amount)
        )
    }

    private fun saveTransfer(transfer: Transfer) {
        confirmTransferViewModel.saveTransfer(transfer).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.btnConfirm.isEnabled = false
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    Toast.makeText(requireContext(), "OK!", Toast.LENGTH_SHORT).show()
                }

                is StateView.Error -> {
                    binding.btnConfirm.isEnabled = false
                    binding.progressBar.isVisible = false
                    showBottomSheetValidateInputs(message = stateView.message)
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnConfirm.setOnClickListener {

            val transfer = Transfer(
                idUserReceipt = args.user.id,
                idUserSent = FirebaseHelper.getUserId(),
                amount = args.amount
            )

            saveTransfer(transfer)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get()?.cancelTag(tagPicasso)
        _binding = null
    }

}