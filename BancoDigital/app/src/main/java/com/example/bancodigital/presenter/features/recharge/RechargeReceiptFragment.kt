package com.example.bancodigital.presenter.features.recharge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bancodigital.R
import com.example.bancodigital.databinding.FragmentHomeBinding
import com.example.bancodigital.databinding.FragmentRechargeRecepitBinding

class RechargeReceiptFragment : Fragment() {

    private var _binding: FragmentRechargeRecepitBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRechargeRecepitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}