package com.erikmobiledev.simpletask.presentation.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<Task> // Asegúrate de que esto sea una función suspend

    @Delete
    suspend fun delete(task: Task)
}
