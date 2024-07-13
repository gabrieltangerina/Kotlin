package com.example.taskapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.R
import com.example.taskapp.data.db.AppDatabase
import com.example.taskapp.data.db.repository.TaskRepository
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task
import com.example.taskapp.databinding.FragmentTasksBinding
import com.example.taskapp.ui.adapter.TaskAdapter
import com.example.taskapp.util.StateView
import com.example.taskapp.util.showBottomSheet


class TaskFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private val viewModel: TaskListViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                val database = AppDatabase.getDatabase(requireContext())
                val repository = TaskRepository(database.taskDao())

                if (modelClass.isAssignableFrom(TaskListViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return TaskListViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initRecyclerTask()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllTasks()
    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            val action = TaskFragmentDirections
                .actionTaskFragmentToFormTaskFragment(null)

            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {
        viewModel.taskList.observe(viewLifecycleOwner){taskList ->
            taskAdapter.submitList(taskList)
            listEmpty(taskList)
        }
    }

    private fun initRecyclerTask() {
        taskAdapter = TaskAdapter { task, option ->
            optionSelected(task, option)
        }

        with(binding.rvTasks) {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = taskAdapter
        }
    }

    private fun optionSelected(task: Task, option: Int) {
//        when (option) {
//            TaskAdapter.SELECT_REMOVE -> {
//                showBottomSheet(
//                    titleDialog = R.string.text_title_dialog_confirm_delete_task,
//                    message = getString(R.string.text_message_dialog_confirm_delete_task),
//                    titleButton = R.string.text_button_dialog_confirm_delete_task,
//                    onClick = {
//                        viewModel.deleteTask(task)
//                    }
//                )
//            }
//
//            TaskAdapter.SELECT_EDIT -> {
//                val action = TaskFragmentDirections
//                    .actionTaskFragmentToFormTaskFragment(task)
//
//                findNavController().navigate(action)
//            }
//
//            TaskAdapter.SELECT_DETAILS -> {
//                Toast.makeText(requireContext(), "Detalhes ${task.description}", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
    }

    private fun listEmpty(taskList: List<Task>) {
        binding.textInfo.text = if (taskList.isEmpty()) {
            getString(R.string.text_list_task_empty)
        } else {
            ""
        }
        binding.progressBar.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}