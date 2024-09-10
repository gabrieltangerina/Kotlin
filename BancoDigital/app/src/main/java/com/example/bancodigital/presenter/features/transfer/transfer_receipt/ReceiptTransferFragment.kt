package com.example.bancodigital.presenter.features.transfer.transfer_receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.R
import com.example.bancodigital.data.model.Transfer
import com.example.bancodigital.data.model.User
import com.example.bancodigital.databinding.FragmentTransferReceiptBinding
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheetValidateInputs
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReceiptTransferFragment : Fragment() {

    private var _binding: FragmentTransferReceiptBinding? = null
    private val binding get() = _binding!!

    private val args: ReceiptTransferFragmentArgs by navArgs()
    private val receiptTransferViewModel: ReceiptTransferViewModel by viewModels()

    private val tagPicasso = "tagPicasso"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(toolbar = binding.toolbar, showIconNavigation = args.showIconNavigation)
        initListeners()
        getTransfer()
    }

    private fun getTransfer() {
        receiptTransferViewModel.getTransfer(args.idTransfer)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is StateView.Success -> {
                        stateView.data?.let {
                            configTransfer(it)

                            val userId = if (it.idUserSent == FirebaseHelper.getUserId()) {
                                it.idUserReceipt
                            } else {
                                it.idUserSent
                            }

                            getProfile(userId)
                        }
                    }

                    is StateView.Error -> {
                        binding.progressBar.isVisible = false
                        showBottomSheetValidateInputs(message = stateView.message)
                    }
                }
            }
    }

    private fun configTransfer(transfer: Transfer) {

        binding.textSendOrReceived.text = if (transfer.idUserSent == FirebaseHelper.getUserId()){
            getString(R.string.text_message_send_receipt_transfer_fragment)
        }else{
            getString(R.string.text_message_received_receipt_transfer_fragment)
        }

        binding.textCodeTransaction.text = transfer.id
        binding.textDataTransaction.text = GetMask.getFormatedDate(
            transfer.date,
            GetMask.DAY_MONTH_YEAR_HOUR_MINUTE
        )

        binding.textAmountTransaction.text = getString(
            R.string.text_balance_format_value,
            GetMask.getFormatedValue(transfer.amount)
        )
    }

    private fun getProfile(id: String) {
        receiptTransferViewModel.getProfile(id).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    stateView.data?.let {
                        configProfile(it)
                    }
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheetValidateInputs(message = stateView.message)
                }
            }
        }
    }

    private fun configProfile(profile: User) {

        if (profile.image != "") {
            Picasso.get()
                .load(profile.image)
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

        binding.textUsername.text = profile.name
    }

    private fun initListeners() {
        binding.btnOk.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get()?.cancelTag(tagPicasso)
        _binding = null
    }

}