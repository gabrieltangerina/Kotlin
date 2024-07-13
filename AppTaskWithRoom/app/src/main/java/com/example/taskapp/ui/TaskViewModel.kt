package com.example.taskapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.R
import com.example.taskapp.data.db.entity.toTaskEntitiy
import com.example.taskapp.data.db.repository.TaskRepository
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task
import com.example.taskapp.util.StateView
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Eventos de dados (inserir, atualizar, listar, deletar)
    private val _taskStateData = MutableLiveData<StateTask>()
    val taskStateData: LiveData<StateTask> = _taskStateData

    // Eventos de mensagens (mensagens de erro, sucesso)
    private val _taskStateMessage = MutableLiveData<Int>()
    val taskStateMessage: LiveData<Int> = _taskStateMessage

    fun insertOrUpdateTask(
        id: Long = 0,
        description: String,
        status: Status
    ) {
        if (id == 0L) {
            insertTask(Task(description = description, status = status))
        } else {
            updateTask(Task(id, description, status))
        }
    }

    fun getTasks() {

    }

    private fun insertTask(task: Task) = viewModelScope.launch {
        try {

            val id = repository.insertTask(task.toTaskEntitiy())
            if (id > 0) {
                _taskStateData.postValue(StateTask.Insert)
                _taskStateMessage.postValue(R.string.text_save_success_form_task_fragment)
            }

        } catch (ex: Exception) {
            _taskStateMessage.postValue(R.string.text_save_error_form_task_fragment)
        }
    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        try {

            repository.updateTask(task.toTaskEntitiy())

            _taskStateData.postValue(StateTask.Insert)
            _taskStateMessage.postValue(R.string.text_update_success_form_task_fragment)

        } catch (ex: Exception) {
            _taskStateMessage.postValue(R.string.text_update_error_form_task_fragment)
        }
    }

    fun deleteTask(task: Task) {

    }

}

sealed class StateTask {
    object Insert : StateTask()
    object Update : StateTask()
    object Delete : StateTask()
    object List : StateTask()
}