package com.example.movieapp.presenter.main.bottombar.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentProfileBinding
import com.example.movieapp.domain.model.MenuProfile
import com.example.movieapp.domain.model.MenuProfileType
import com.example.movieapp.presenter.main.bottombar.profile.adapter.ProfileMenuAdapter

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter : ProfileMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configData()
        initRecycler()
    }

    private fun initRecycler(){
        mAdapter = ProfileMenuAdapter(
            context = requireContext(),
            onClick = {type ->
                when(type){
                    MenuProfileType.PROFILE -> {

                    }
                    MenuProfileType.NOTIFICATION -> {

                    }
                    MenuProfileType.DOWNLOAD -> {

                    }
                    MenuProfileType.SECURITY -> {

                    }
                    MenuProfileType.LANGUAGE -> {

                    }
                    MenuProfileType.DARK_MODE -> {

                    }
                    MenuProfileType.HELPER -> {

                    }
                    MenuProfileType.PRIVACY_POLICY -> {

                    }
                    MenuProfileType.LOGOUT -> {

                    }
                }
            }
        )

        with(binding.recyclerItems){
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun configData(){
        binding.imageProfile.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.no_user_picture
            )
        )

        binding.textUsername.text = "Gabriel Tangerina"
        binding.textEmail.text = "gabriel@example.com"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}