package com.example.taskapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskapp.R
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task
import com.example.taskapp.databinding.FragmentToDoBinding
import com.example.taskapp.ui.adapter.TaskAdapter


class ToDoFragment : Fragment() {

    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        initRecyclerTask(getTasks())
    }

    private fun initListeners(){
        binding.fabAdd.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment)
        }
    }

    private fun initRecyclerTask(taskList: List<Task>){
        taskAdapter = TaskAdapter(requireContext(), taskList)

        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTasks.setHasFixedSize(true)
        binding.rvTasks.adapter = taskAdapter
    }

    private fun getTasks() = listOf(
        Task("0", "Passear com o cachorro", Status.TODO),
        Task("1", "Ir ao mercado", Status.TODO),
        Task("2", "Estudar sobre Kotlin", Status.TODO),
        Task("3", "Assistir filme", Status.TODO),
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}