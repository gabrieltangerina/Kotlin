package com.example.taskapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskapp.data.db.repository.TaskRepository
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task
import com.example.taskapp.util.StateView

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // Eventos de dados (inserir, atualizar, listar, deletar)
    private val _taskStateData = MutableLiveData<StateTask>()
    val taskStateData : LiveData<StateTask> = _taskStateData

    // Eventos de mensagens (mensagens de erro, sucesso)
    private val _taskStateMessage = MutableLiveData<Int>()
    val taskStateMessage : LiveData<Int> = _taskStateMessage

    fun insertOrUpdateTask(
        id: Long = 0,
        description: String,
        status: Status
    ){
        if(id == 0L){
            insertTask(Task(description = description, status = status))
        }else{
            updateTask(Task(id, description, status))
        }
    }

    fun getTasks() {

    }

    private fun insertTask(task: Task) {

    }

    private fun updateTask(task: Task){

    }

    fun deleteTask(task: Task) {

    }

}

sealed class StateTask{
    object Insert: StateTask()
    object Update: StateTask()
    object Delete: StateTask()
    object List: StateTask()
}