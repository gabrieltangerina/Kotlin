package com.example.bancodigital.presenter.auth.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.bancodigital.R
import com.example.bancodigital.data.model.User
import com.example.bancodigital.data.model.Wallet
import com.example.bancodigital.databinding.FragmentRegisterBinding
import com.example.bancodigital.presenter.profile.ProfileViewModel
import com.example.bancodigital.presenter.wallet.WalletViewModel
import com.example.bancodigital.util.FirebaseHelper
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val walletViewModel: WalletViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        initListeners()
    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val name = binding.editName.text.toString().trim()
        val email = binding.editEmail.text.toString().trim()
        val phone = binding.editPhone.unMaskedText
        val password = binding.editPassword.text.toString().trim()
        val confirmPassword = binding.editConfirmPassword.text.toString().trim()

        if (name.isEmpty()) {
            showBottomSheet(message = getString(R.string.text_name_empty))
            return
        }

        if (email.isEmpty()) {
            showBottomSheet(message = getString(R.string.text_email_empty))
            return
        }

        if (phone?.isEmpty() == true) {
            showBottomSheet(message = getString(R.string.text_phone_empty))
            return
        }

        if (phone?.length != 11) {
            showBottomSheet(message = getString(R.string.text_phone_invalid))
            return
        }

        if (password.isEmpty()) {
            showBottomSheet(message = getString(R.string.text_password_empty))
            return
        }

        if (confirmPassword.isEmpty()) {
            showBottomSheet(message = getString(R.string.text_confirm_password_empty))
            return
        }

        if (password != confirmPassword) {
            showBottomSheet(message = getString(R.string.fields_password_not_equals))
            return
        }


        registerUser(name, email, phone, password)

    }

    // Save in the Firebase Authentication
    private fun registerUser(name: String, email: String, phone: String, password: String) {
        registerViewModel.register(name, email, phone, password)
            .observe(viewLifecycleOwner) { stateView ->
                when (stateView) {
                    is StateView.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is StateView.Sucess -> {
                        val user = User(
                            name = name,
                            id = FirebaseHelper.getUserId(),
                            email = email,
                            phone = phone
                        )
                        saveProfile(user)
                    }

                    is StateView.Error -> {
                        binding.progressBar.isVisible = false
                        showBottomSheet(
                            message = getString(
                                FirebaseHelper.validError(
                                    stateView.message ?: ""
                                )
                            )
                        )
                    }
                }
            }
    }

    // Save in the Firebase Database
    private fun saveProfile(user: User) {
        profileViewModel.saveProfile(user).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Sucess -> {
                    initWallet()
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(
                        message = getString(
                            FirebaseHelper.validError(
                                stateView.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    private fun initWallet() {
        walletViewModel.initWallet(Wallet(
            userId = FirebaseHelper.getUserId()
        )).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Sucess -> {
                    binding.progressBar.isVisible = false
                    findNavController().navigate(R.id.action_global_homeFragment)
                }

                is StateView.Error -> {
                    binding.progressBar.isVisible = false
                    showBottomSheet(
                        message = getString(
                            FirebaseHelper.validError(
                                stateView.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}