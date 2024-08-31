package com.example.bancodigital.presenter.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bancodigital.R
import com.example.bancodigital.data.model.User
import com.example.bancodigital.databinding.FragmentProfileBinding
import com.example.bancodigital.util.StateView
import com.example.bancodigital.util.hideKeyboard
import com.example.bancodigital.util.initToolbar
import com.example.bancodigital.util.showBottomSheetProfile
import com.example.bancodigital.util.showBottomSheetValidateInputs
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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

    private var imageProfile: String? = null
    private var currentPhotoPath: String? = null

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
                    showBottomSheetValidateInputs(message = stateView.message)
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnUpdate.setOnClickListener {
            updateProfile()
        }

        binding.imageProfile.setOnClickListener {
            showBottomSheetProfile(
                { openGallery() },
                { openCamera() }
            )
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun openCamera(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        
        var photoFile: File? = null
        try{
            photoFile = createImageFile()
        }catch (ex: IOException){
            Toast.makeText(requireContext(), "Não foi possível abrir a camera do dispositivo", Toast.LENGTH_SHORT).show()
        }

        if(photoFile != null){
            val photoURI = FileProvider.getUriForFile(
                requireContext(),
                "com.example.bancodigital.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            cameraLauncher.launch(takePictureIntent)
        }
    }

    private fun createImageFile():File{
        val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale("pt", "BR")).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        currentPhotoPath = image.absolutePath
        return image
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val file = File(currentPhotoPath!!)
            binding.imageProfile.setImageURI(Uri.fromFile(file))

            imageProfile = file.toURI().toString()
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            val imageSelected = result.data!!.data
            imageProfile = imageSelected.toString()

            if (imageSelected != null) {
                binding.imageProfile.setImageBitmap(getBitmap(imageSelected))
            }

        }
    }

    private fun getBitmap(pathUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {

            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, pathUri)
            } else {
                val source = ImageDecoder.createSource(requireActivity().contentResolver, pathUri)
                ImageDecoder.decodeBitmap(source)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return bitmap
    }

    private fun updateProfile() {
        hideKeyboard()
        val name = binding.editName.text.toString().trim()
        val phone = binding.editPhone.unMaskedText.toString()

        val profile = User(
            id = initialUser?.id ?: "",
            name = name,
            phone = phone,
            email = initialUser?.email ?: ""
        )

        profileViewModel.updateProfile(profile).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
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

                if ((flagChangedName && nameValid) || (flagChangedPhone && phoneValid)) {
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

                if ((flagChangedName && nameValid) || (flagChangedPhone && phoneValid)) {
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