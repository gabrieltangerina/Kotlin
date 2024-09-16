package com.example.movieapp.presenter.main.bottombar.profile.edit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.movieapp.R
import com.example.movieapp.databinding.BottomSheetPermissionDeniedBinding
import com.example.movieapp.databinding.BottomSheetSelectImageBinding
import com.example.movieapp.databinding.FragmentEditProfileBinding
import com.example.movieapp.domain.model.user.User
import com.example.movieapp.util.FirebaseHelper
import com.example.movieapp.util.StateView
import com.example.movieapp.util.initToolbar
import com.example.movieapp.util.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditProfileViewModel by viewModels()


    private val GALLERY_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    private val CAMERA_PERMISSION = Manifest.permission.CAMERA

    private var currentPhotoPath: String? = null
    private lateinit var photoUri: Uri

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

    private fun getEmailUser() {
        binding.editEmail.setText(FirebaseHelper.getAuth().currentUser?.email)
    }

    private fun initListeners() {
        binding.btnUpdate.setOnClickListener {
            validateData()
        }

        binding.imageProfile.setOnClickListener {
            openBottomSheetSelectImage()
        }
    }

    private fun openBottomSheetSelectImage() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)

        val bottomSheetBinding = BottomSheetSelectImageBinding.inflate(
            layoutInflater, null, false
        )

        bottomSheetBinding.btnCamera.setOnClickListener {
            bottomSheetDialog.dismiss()
            checkCameraPermission()
        }

        bottomSheetBinding.btnGallery.setOnClickListener {
            bottomSheetDialog.dismiss()
            checkGalleryPermission()
        }

        bottomSheetBinding.btnClose.setOnClickListener { bottomSheetDialog.dismiss() }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun openBottomSheetPermissionDenied() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)

        val bottomSheetBinding = BottomSheetPermissionDeniedBinding.inflate(
            layoutInflater, null, false
        )

        bottomSheetBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.btnAccept.setOnClickListener {
            bottomSheetDialog.dismiss()

            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", requireActivity().packageName, null)
            )

            startActivity(intent)
        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun checkGalleryPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            // Android 12-
            if (checkPermissionGranted(GALLERY_PERMISSION)) {
                pickImageLauncher.launch("image/*")
            } else {
                galleryPermissionLauncher.launch(GALLERY_PERMISSION)
            }
        } else {
            // Android 13+
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun checkCameraPermission() {
        if (checkPermissionGranted(CAMERA_PERMISSION)) {
            openCamera()
        } else {
            cameraPermissionLauncher.launch(CAMERA_PERMISSION)
        }
    }

    // Camera
    private val cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                openBottomSheetPermissionDenied()
            }
        }

    // Camera
    private fun openCamera() {
        val photoFile = createImageFile()
        photoFile?.let {
            photoUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.provider",
                it
            )
            takePictureLauncher.launch(photoUri)
        }
    }

    // Camera
    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            binding.imageProfile.setImageURI(photoUri)
        }
    }

    // Camera
    private fun createImageFile(): File? {
        val timeStamp: String =
            SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(Date())

        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val imageFile = File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )

        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    // Camera - Gallery
    private fun checkPermissionGranted(permission: String) =
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private val galleryPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                pickImageLauncher.launch("image/*")
            } else {
                openBottomSheetPermissionDenied()
            }
        }

    // Used in Android <= 12
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            binding.imageProfile.setImageURI(it)
        }
    }

    // Used in Android >= 13
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                binding.imageProfile.setImageURI(it)
            }
        }

    private fun validateData() {
        val name = binding.editName.text.toString().trim()
        val username = binding.editUser.text.toString().trim()
        val phone = binding.editPhone.text.toString().trim()
        val genre = binding.editGenre.text.toString().trim()
        val country = binding.editCountry.text.toString().trim()

        if (name.isEmpty()) {
            showSnackBar(message = R.string.text_name_empty_edit_profile_fragment)
            return
        } else if (username.isEmpty()) {
            showSnackBar(message = R.string.text_user_empty_edit_profile_fragment)
            return
        } else if (phone.isEmpty()) {
            showSnackBar(message = R.string.text_phone_empty_edit_profile_fragment)
            return
        } else if (genre.isEmpty()) {
            showSnackBar(message = R.string.text_genre_empty_edit_profile_fragment)
            return
        } else if (country.isEmpty()) {
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
        viewModel.update(user).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
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
        viewModel.getUser().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
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

    private fun configData(user: User) {
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