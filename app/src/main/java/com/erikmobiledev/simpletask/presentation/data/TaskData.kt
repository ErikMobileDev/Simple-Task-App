package com.erikmobiledev.simpletask.presentation.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String, // Identificador único
    val title: String,
    val firstHour: String,
    val secondHour: String? = null
)
