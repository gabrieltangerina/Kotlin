package com.example.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fragment.databinding.FragmentBBinding

class FragmentB : Fragment() {

    private var _binding:FragmentBBinding? = null
    private val binding get() = _binding!!

    private val args:FragmentBArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        getExtras()
    }

    /*
    * Recebe os dados passados do Fragment A
    */
    private fun getExtras(){
        Toast.makeText(requireContext(), "${args.name}", Toast.LENGTH_SHORT).show()
    }

    /*
    * Devolve dados para o Fragment A
    */
    private fun initListeners(){
        binding.btnBack.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                "KEY",
                bundleOf(Pair("KEY", "Devolvendo dados\nFragment B -> Fragment A"))
            )

            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}