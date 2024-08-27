package com.example.bancodigital.presenter.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bancodigital.R
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
    private var nameValid: Boolean = false
    private var phoneValid: Boolean = false

    private var initialUser: User? = null

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
        initListeners()
    }

    private fun getProfile() {
        profileViewModel.getProfileUseCase().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false

                    stateView.data?.let {
                        configData(it)
                        initialUser = it
                    }

                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(message = stateView.message)
                }
            }
        }
    }

    private fun initListeners(){
        binding.btnUpdate.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile(){
        val name = binding.editName.text.toString().trim()
        val phone = binding.editPhone.unMaskedText.toString()

        val profile = User(
            id = initialUser?.id ?: "",
            name = name,
            phone = phone,
            email = initialUser?.email ?: ""
        )

        profileViewModel.updateProfile(profile).observe(viewLifecycleOwner){stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.progressBar.isVisible = false

                    stateView.data?.let {
                        configData(it)
                    }
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                }
            }
        }
    }

    private fun configData(user: User) {
        binding.editEmail.setText(user.email)
        binding.editName.setText(user.name)
        binding.editPhone.setText(user.phone)

        initialName = binding.editName.text.toString()
        initialPhone = binding.editPhone.text.toString()
        initObservers()
    }

    private fun initObservers() {
        binding.editName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                flagChangedName = s.toString().trim() != initialName
                nameValid = binding.editName.text.toString().trim() != ""

                if ((flagChangedName && nameValid) || ( flagChangedPhone && phoneValid)) {
                    binding.btnUpdate.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.bg_btn
                        )
                    )
                    binding.btnUpdate.isEnabled = true
                } else {
                    binding.btnUpdate.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.bg_btn_not_enabled
                        )
                    )
                    binding.btnUpdate.isEnabled = false
                }

            }

            override fun afterTextChanged(s: Editable?) {
                return
            }

        })

        binding.editPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                flagChangedPhone = s.toString().trim() != initialPhone
                phoneValid = binding.editPhone.text.toString().length == 15

                if ((flagChangedName && nameValid) || ( flagChangedPhone && phoneValid)) {
                    binding.btnUpdate.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.bg_btn
                        )
                    )
                    binding.btnUpdate.isEnabled = true
                } else {
                    binding.btnUpdate.setBackgroundDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.bg_btn_not_enabled
                        )
                    )
                    binding.btnUpdate.isEnabled = false
                }

            }

            override fun afterTextChanged(s: Editable?) {
                return
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}