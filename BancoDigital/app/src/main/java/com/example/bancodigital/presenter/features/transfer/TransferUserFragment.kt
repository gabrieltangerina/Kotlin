package com.example.bancodigital.presenter.features.transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bancodigital.R
import com.example.bancodigital.data.model.User
import com.example.bancodigital.databinding.FragmentTransferUserBinding
import com.example.bancodigital.presenter.features.transfer.adapter.TransferUserAdapter
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheetValidateInputs
import com.ferfalk.simplesearchview.SimpleSearchView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransferUserFragment : Fragment() {

    private var _binding: FragmentTransferUserBinding? = null
    private val binding get() = _binding!!

    private val transferUserViewModel: TransferUserViewModel by viewModels()
    private lateinit var adapterTransferUser: TransferUserAdapter

    private var profilesList: List<User> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransferUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar, icon = R.drawable.ic_back_white)
        configRecyclerView()
        getProfiles()
        configSearchView()
    }

    private fun configSearchView() {

        binding.searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return if (newText.isNotEmpty()) {

                    val newList = adapterTransferUser.currentList.filter {
                        it.name.contains(
                            newText,
                            ignoreCase = true
                        )
                    }
                    adapterTransferUser.submitList(newList)
                    true
                } else {
                    adapterTransferUser.submitList(profilesList)
                    false
                }
            }

            override fun onQueryTextCleared(): Boolean {
                return false
            }
        })

        binding.searchView.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
            override fun onSearchViewShown() {

            }

            override fun onSearchViewClosed() {
                adapterTransferUser.submitList(profilesList)
            }

            override fun onSearchViewShownAnimation() {

            }

            override fun onSearchViewClosedAnimation() {

            }
        })
    }

    private fun getProfiles() {
        transferUserViewModel.getProfilesUseCase().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false
                    profilesList = stateView.data ?: emptyList()
                    adapterTransferUser.submitList(profilesList)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheetValidateInputs(message = stateView.message)
                }
            }
        }
    }

    private fun configRecyclerView() {
        adapterTransferUser = TransferUserAdapter { user ->
            val action =
                TransferUserFragmentDirections.actionTransferUserFragmentToTransferFormFragment(user)

            findNavController().navigate(action)
        }

        with(binding.rvUsers) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterTransferUser
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val item = menu.findItem(R.id.action_search)
        binding.searchView.setMenuItem(item)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}