package com.example.movieapp.presenter.main.bottombar.profile.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentEditProfileBinding
import com.example.movieapp.databinding.FragmentProfileBinding
import com.example.movieapp.util.initToolbar
import com.example.movieapp.util.isEmailValid
import com.example.movieapp.util.showSnackBar


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

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
        initListeners()
    }

    private fun initListeners(){
        binding.btnUpdate.setOnClickListener {
            validateData()
        }
    }

    private fun validateData(){
        val name = binding.editName.text.toString().trim()
        val lastName = binding.editUser.text.toString().trim()
        val phone = binding.editPhone.text.toString().trim()
        val genre = binding.editGenre.text.toString().trim()
        val country = binding.editCountry.text.toString().trim()

        if(name.isEmpty()){
            showSnackBar(message = R.string.text_name_empty_edit_profile_fragment)
        }else if(lastName.isEmpty()){
            showSnackBar(message = R.string.text_user_empty_edit_profile_fragment)
        }else if(phone.isEmpty()){
            showSnackBar(message = R.string.text_phone_empty_edit_profile_fragment)
        }else if(genre.isEmpty()){
            showSnackBar(message = R.string.text_genre_empty_edit_profile_fragment)
        }else if(country.isEmpty()){
            showSnackBar(message = R.string.text_country_empty_edit_profile_fragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}