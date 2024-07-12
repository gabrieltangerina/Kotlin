package com.example.taskapp.data.db.repository

import com.example.taskapp.data.db.dao.TaskDao
import com.example.taskapp.data.db.entity.TaskEntity
import com.example.taskapp.data.model.Task

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