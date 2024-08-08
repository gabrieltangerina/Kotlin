package com.example.movieapp.presenter.main.bottombar.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.R
import com.example.movieapp.databinding.BottomSheetLogoutBinding
import com.example.movieapp.databinding.FragmentProfileBinding
import com.example.movieapp.domain.model.MenuProfileType
import com.example.movieapp.presenter.auth.activity.AuthActivity
import com.example.movieapp.presenter.auth.activity.AuthActivity.Companion.AUTHENTICATION_PARAMETER
import com.example.movieapp.presenter.auth.enums.AuthenticationDestinations
import com.example.movieapp.presenter.main.bottombar.profile.adapter.ProfileMenuAdapter
import com.example.movieapp.util.FirebaseHelper
import com.example.movieapp.util.StateView
import com.example.movieapp.util.showSnackBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter : ProfileMenuAdapter
    private val viewModel: ProfileViewModel by viewModels()

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
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        binding.frameLayout.setOnClickListener{
            findNavController().navigate(R.id.action_menu_profile_to_editProfileFragment)
        }
    }

    private fun initObservers(){
        viewModel.profileState.observe(viewLifecycleOwner){stateView ->
            when(stateView){
                is StateView.Loading -> {
                    binding.layoutLoading.isVisible = true
                    binding.progressBar.isVisible = true
                }

                is StateView.Success -> {
                    binding.layoutLoading.isVisible = false
                    binding.progressBar.isVisible = false

                    activity?.finish()
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    intent.putExtra(AUTHENTICATION_PARAMETER, AuthenticationDestinations.LOGIN_SCREEN)
                    startActivity(intent)
                }
                is StateView.Error -> {
                    binding.layoutLoading.isVisible = false
                    binding.progressBar.isVisible = false
                    showSnackBar(message = FirebaseHelper.validError(stateView.message ?: ""))
                }
            }
        }
    }

    private fun initRecycler(){
        mAdapter = ProfileMenuAdapter(
            context = requireContext(),
            onClick = {type ->
                when(type){
                    MenuProfileType.PROFILE -> {
                        findNavController().navigate(R.id.action_menu_profile_to_editProfileFragment)
                    }
                    MenuProfileType.NOTIFICATION -> {

                    }
                    MenuProfileType.DOWNLOAD -> {
                        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.btnv)
                        bottomNavigation?.selectedItemId = R.id.menu_download
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
                        showBottomSheetLogout()
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

    private fun showBottomSheetLogout() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val bottomSheetBinding = BottomSheetLogoutBinding.inflate(
            layoutInflater, null, false
        )

        bottomSheetBinding.btnCancel.setOnClickListener { bottomSheetDialog.dismiss() }
        bottomSheetBinding.btnConfirm.setOnClickListener { logout() }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()

    }

    private fun logout(){
        viewModel.logout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}