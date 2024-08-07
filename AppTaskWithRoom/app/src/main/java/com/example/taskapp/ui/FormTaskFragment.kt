package com.example.taskapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskapp.R
import com.example.taskapp.data.db.AppDatabase
import com.example.taskapp.data.db.repository.TaskRepository
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task
import com.example.taskapp.databinding.FragmentFormTaskBinding
import com.example.taskapp.util.initToolbar
import com.example.taskapp.util.showBottomSheet

class FormTaskFragment :BaseFragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var status: Status = Status.TODO
    private var newTask: Boolean = true

    private val args: FormTaskFragmentArgs by navArgs()

    /*
    * activityViewModels() centraliza os dados no contexto da activity, podendo acessa-los
    * independente se o fragment outros fragments que tem acesso ao viewModel estão
    * abertos ou não
    */
    private val viewModel:TaskViewModel by viewModels {
        object :  ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                val database = AppDatabase.getDatabase(requireContext())
                val repository = TaskRepository(database.taskDao())

                if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return TaskViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        getArgs()
        initListeners()
    }

    private fun getArgs(){
        args.task.let { it ->
            if(it != null){
                this.task = it
                configTask()
            }
        }
    }

    private fun configTask(){
        newTask = false
        status = task.status
        binding.textToolbar.setText(R.string.text_toolbar_update)
        binding.editDescription.setText(task.description)
        setStatus()
    }

    private fun setStatus(){
        binding.rgStatus.check(
            when(task.status){
                Status.TODO -> R.id.rbTodo
                Status.DOING -> R.id.rbDoing
                else -> R.id.rbDone
            }
        )
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener {
            observeViewModel()
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
            hideKeyboard()
            binding.progressBar.isVisible = true

            if(newTask) task = Task()
            task.description = description
            task.status = status

            if(newTask){
                viewModel.insertOrUpdateTask(description = description, status = status)
            }else{
                viewModel.insertOrUpdateTask(id = task.id, description = description, status = status)
            }

        } else {
            showBottomSheet(message = getString(R.string.description_empty_warning_form))
        }
    }

    private fun observeViewModel() {
        viewModel.taskStateData.observe(viewLifecycleOwner){stateTask ->
            if(stateTask == StateTask.Insert || stateTask == StateTask.Update){
                findNavController().popBackStack()
            }
        }

        viewModel.taskStateMessage.observe(viewLifecycleOwner){message ->
            Toast.makeText(requireContext(), getString(message), Toast.LENGTH_SHORT).show()
            binding.progressBar.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}