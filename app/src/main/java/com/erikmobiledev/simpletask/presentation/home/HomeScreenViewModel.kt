package com.erikmobiledev.simpletask.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erikmobiledev.simpletask.presentation.data.Task
import java.util.UUID
import androidx.lifecycle.viewModelScope
import com.erikmobiledev.simpletask.presentation.data.TaskDao
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val dao: TaskDao) : ViewModel() {
    private val _tasks = MutableLiveData<List<Task>>(emptyList())
    val tasks: LiveData<List<Task>> = _tasks

    init {
        loadTasks() // Cargar tareas al inicializar el ViewModel
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _tasks.value = dao.getAllTasks() // Obtener tareas de la base de datos
        }
    }

    fun addTask(title: String, firstHour: String, secondHour: String? = null) {
        val newTask = Task(
            id = UUID.randomUUID().toString(),
            title = title,
            firstHour = firstHour,
            secondHour = secondHour
        )
        viewModelScope.launch {
            dao.insert(newTask) // Insertar la tarea en la base de datos
            loadTasks() // Cargar tareas de nuevo para actualizar el LiveData
        }
    }

    fun removeTask(task: Task) {
        viewModelScope.launch {
            dao.delete(task) // Eliminar tarea de la base de datos
            loadTasks() // Cargar tareas de nuevo
        }
    }
}
