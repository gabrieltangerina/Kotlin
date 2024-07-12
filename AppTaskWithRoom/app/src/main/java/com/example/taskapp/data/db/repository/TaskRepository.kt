package com.example.taskapp.data.db.repository

import com.example.taskapp.data.db.dao.TaskDao
import com.example.taskapp.data.db.entity.TaskEntity
import com.example.taskapp.data.model.Task

/*
* Controla de onde vem os nossos dados (localmente ou API).
* De onde vem os dados dependerá da implementação,a
* implementação é o parâmetro do construtor, como
* vem de TaskDao é localmente, mas pode acontecer de
*  implementação ser uma API, por exemplo chamada de
* DataSource, onde as operações que seriam da TaskDAO
* (getAllTask,insertTask...) vão vir dela
 */
class TaskRepository(private val taskDao: TaskDao) {

    suspend fun getAllTask(): List<Task>{
        return taskDao.getAllTask()
    }

    suspend fun insertTask(taskEntity: TaskEntity): Long{
        return taskDao.insertTask(taskEntity)
    }

    suspend fun deleteTask(id: Long){
        return taskDao.deleteTask(id)
    }

    suspend fun updateTask(taskEntity: TaskEntity){
        return taskDao.updateTask(
            taskEntity.id,
            taskEntity.description,
            taskEntity.status
        )
    }

}