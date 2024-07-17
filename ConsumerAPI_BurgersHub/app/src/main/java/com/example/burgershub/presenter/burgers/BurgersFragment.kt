package com.example.burgershub.presenter.burgers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.burgershub.databinding.FragmentBurgersBinding
import com.example.burgershub.domain.model.Burger
import com.example.burgershub.util.StateView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BurgersFragment : Fragment() {

    private var _binding: FragmentBurgersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BurgerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBurgersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getBurgers()
        initListeners()
    }

    private fun initListeners() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(name: String?): Boolean {
                return if (name != null) {
                    getBurgerByName(name)
                    true
                } else false

            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun getBurgers() {
        viewModel.getBurgers().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {

                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.rvBurgers.isVisible = false
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    binding.rvBurgers.isVisible = true

                    val burgers = stateView.data ?: emptyList()
                    initRecycler(burgers)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    binding.rvBurgers.isVisible = true
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun getBurgerByName(name: String) {
        viewModel.getBurgerByName(name).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                    binding.rvBurgers.isVisible = false
                }
                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    binding.rvBurgers.isVisible = true

                    val burgers = stateView.data ?: emptyList()
                    initRecycler(burgers)
                }
                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    binding.rvBurgers.isVisible = true
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initRecycler(burgers: List<Burger>) {
        with(binding.rvBurgers) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = BurgersAdapter(burgers) { burgerId ->
                val action = BurgersFragmentDirections
                    .actionBurgersFragmentToDetailsFragment(burgerId)

                findNavController().navigate(action)
            }
        }

        val closeButton: View? = binding.searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        closeButton?.setOnClickListener {
            binding.searchView.setQuery("", false)
            binding.searchView.clearFocus()
            getBurgers()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}