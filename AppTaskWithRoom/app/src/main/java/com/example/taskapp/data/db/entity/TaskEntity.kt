package com.example.taskapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskapp.data.model.Status
import com.example.taskapp.data.model.Task

@Entity(tableName = "task_table")
class TaskEntity(
    @PrimaryKey(true)
    val id: Long,

    val description: String,

    val status: Status
)

fun Task.toTaskEntitiy(): TaskEntity {
    return with(this){
        TaskEntity(
            id = this.id,
            description = this.description,
            status = this.status
        )
    }
}