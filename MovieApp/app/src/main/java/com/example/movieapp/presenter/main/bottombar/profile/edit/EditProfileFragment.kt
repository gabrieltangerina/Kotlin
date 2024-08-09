package com.example.movieapp.presenter.main.bottombar.profile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentEditProfileBinding
import com.example.movieapp.domain.model.user.User
import com.example.movieapp.util.FirebaseHelper
import com.example.movieapp.util.StateView
import com.example.movieapp.util.initToolbar
import com.example.movieapp.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        getEmailUser()
        getUser()
        initListeners()
    }

    private fun getEmailUser(){
        binding.editEmail.setText(FirebaseHelper.getAuth().currentUser?.email)
    }

    private fun initListeners(){
        binding.btnUpdate.setOnClickListener {
            validateData()
        }
    }

    private fun validateData(){
        val name = binding.editName.text.toString().trim()
        val username = binding.editUser.text.toString().trim()
        val phone = binding.editPhone.text.toString().trim()
        val genre = binding.editGenre.text.toString().trim()
        val country = binding.editCountry.text.toString().trim()

        if(name.isEmpty()){
            showSnackBar(message = R.string.text_name_empty_edit_profile_fragment)
            return
        }else if(username.isEmpty()){
            showSnackBar(message = R.string.text_user_empty_edit_profile_fragment)
            return
        }else if(phone.isEmpty()){
            showSnackBar(message = R.string.text_phone_empty_edit_profile_fragment)
            return
        }else if(genre.isEmpty()){
            showSnackBar(message = R.string.text_genre_empty_edit_profile_fragment)
            return
        }else if(country.isEmpty()){
            showSnackBar(message = R.string.text_country_empty_edit_profile_fragment)
            return
        }

        val user = User(
            id = FirebaseHelper.getUserId(),
            name = name,
            username = username,
            email = FirebaseHelper.getAuth().currentUser?.email,
            phone = phone,
            genre = genre,
            country = country
        )

        update(user)
    }

    private fun update(user: User) {
        viewModel.update(user).observe(viewLifecycleOwner){stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.layoutLoading.isVisible = true
                }

                is StateView.Success -> {
                    binding.layoutLoading.isVisible = false
                    showSnackBar(message = R.string.text_update_success_edit_profile_fragment)
                }

                is StateView.Error -> {
                    binding.layoutLoading.isVisible = false
                    showSnackBar(message = FirebaseHelper.validError(stateView.message ?: ""))
                }
            }
        }
    }

    private fun getUser() {
        viewModel.getUser().observe(viewLifecycleOwner){stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.layoutLoading.isVisible = true
                }

                is StateView.Success -> {
                    binding.layoutLoading.isVisible = false

                    stateView.data?.let {
                        configData(it)
                    }
                }

                is StateView.Error -> {
                    binding.layoutLoading.isVisible = false
                    showSnackBar(message = FirebaseHelper.validError(stateView.message ?: ""))
                }
            }
        }
    }

    private fun configData(user: User){
        binding.editName.setText(user.name)
        binding.editUser.setText(user.username)
        binding.editEmail.setText(FirebaseHelper.getAuth().currentUser?.email)
        binding.editPhone.setText(user.phone)
        binding.editGenre.setText(user.genre)
        binding.editCountry.setText(user.country)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}