package com.example.taskapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.taskapp.R
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task
import com.example.taskapp.databinding.FragmentFormTaskBinding
import com.example.taskapp.util.initToolbar
import com.example.taskapp.util.showBottomSheet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var status: Status = Status.TODO
    private var newTask: Boolean = true

    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reference = Firebase.database.reference
        auth = Firebase.auth

        initToolbar(binding.toolbar)
        initListeners()
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener {
            validateData()
        }

        binding.rgStatus.setOnCheckedChangeListener { _, checkedId ->
            status = when(checkedId){
                R.id.rbTodo -> Status.TODO
                R.id.rbDoing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    private fun validateData() {
        val description = binding.editDescription.text.toString().trim()

        if (description.isNotEmpty()) {
            binding.progressBar.isVisible = true

            if(newTask) task = Task()
            task.id = reference.database.reference.push().key ?: ""
            task.description = description
            task.status = status

            saveTask()
        } else {
            showBottomSheet(message = getString(R.string.description_empty_warning_form))
        }
    }

    private fun saveTask() {
        reference
            .child("tasks")
            .child(auth.currentUser?.uid ?: "")
            .child(task.id)
            .setValue(task).addOnCompleteListener { result ->
                if(result.isSuccessful){
                    Toast.makeText(requireContext(), R.string.text_save_success_form_task_fragment, Toast.LENGTH_SHORT).show()

                    if(newTask){
                        findNavController().popBackStack()
                    }else{
                        binding.progressBar.isVisible = false
                    }

                }else{
                    showBottomSheet(message = getString(R.string.error_generic))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}