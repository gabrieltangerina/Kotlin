package com.example.bancodigital.presenter.features.transfer.transfer_confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.bancodigital.R
import com.example.bancodigital.databinding.FragmentTransferConfirmBinding
import com.example.bancodigital.util.GetMask
import com.example.bancodigital.util.initToolbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfirmTransferFragment : Fragment() {

    private var _binding: FragmentTransferConfirmBinding? = null
    private val binding get() = _binding!!

    private val args: ConfirmTransferFragmentArgs by navArgs()

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

    private fun initListeners() {
        binding.btnContinue.setOnClickListener {
            // findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Picasso.get()?.cancelTag(tagPicasso)
        _binding = null
    }

}