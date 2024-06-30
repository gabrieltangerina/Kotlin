package com.example.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.fragment.databinding.FragmentABinding

class FragmentA : Fragment() {

    private var _binding: FragmentABinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentABinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        listenerFragment()
    }

    /*
    * Passa os dados para o Fragment B
    */
    private fun initListener(){
        val name = "Passando dados\nFragment A -> Fragment B"

        binding.btnNext.setOnClickListener {
            val action = FragmentADirections.actionFragmentAToFragmentB(name)
            findNavController().navigate(action)
        }
    }

    /*
    * Recebe os dados devolvidos do Fragment B
    */
    private fun listenerFragment(){
        parentFragmentManager.setFragmentResultListener(
            "KEY",
            this
        ){key, bundle ->
            val name = bundle[key]
            Toast.makeText(requireContext(), "$name", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}