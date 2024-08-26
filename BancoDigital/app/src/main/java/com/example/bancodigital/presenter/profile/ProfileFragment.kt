package com.example.bancodigital.presenter.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bancodigital.data.model.User
import com.example.bancodigital.databinding.FragmentProfileBinding
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var initialName: String
    private lateinit var initialPhone: String
    private var flagChangedName: Boolean = false
    private var flagChangedPhone: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        getProfile()
        initObservers()
    }

    private fun getProfile(){
        profileViewModel.getProfileUseCase().observe(viewLifecycleOwner){stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false

                    stateView.data?.let {
                        initialName = stateView.data.name
                        initialPhone = stateView.data.phone
                        configData(it)
                    }

                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun initObservers(){
        binding.editName.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                flagChangedName = s.toString().trim() != initialName
                binding.btnUpdate.isEnabled = flagChangedName || flagChangedPhone
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }

        })

        binding.editPhone.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                flagChangedPhone = s.toString().trim() != initialPhone
                binding.btnUpdate.isEnabled = flagChangedName || flagChangedPhone
            }

            override fun afterTextChanged(s: Editable?) {
                return
            }

        })
    }

    private fun configData(user: User){
        binding.editEmail.setText(user.email)
        binding.editName.setText(user.name)
        binding.editPhone.setText(user.phone)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}