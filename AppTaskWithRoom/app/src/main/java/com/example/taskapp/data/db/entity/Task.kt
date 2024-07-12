package com.example.taskapp.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.taskapp.data.model.Status

@Entity(tableName = "task_table")
class Task (
    @PrimaryKey(true)
    val id: Long,

    val description: String,

    val status: Status
)