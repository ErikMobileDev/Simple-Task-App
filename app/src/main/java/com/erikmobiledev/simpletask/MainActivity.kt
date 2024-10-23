package com.erikmobiledev.simpletask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.erikmobiledev.simpletask.presentation.data.TaskDatabase
import com.erikmobiledev.simpletask.presentation.home.HomeScreen
import com.erikmobiledev.simpletask.presentation.home.HomeScreenViewModel
import com.erikmobiledev.simpletask.presentation.home.HomeScreenViewModelFactory
import com.erikmobiledev.simpletask.presentation.theme.TaskManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = TaskDatabase.getDatabase(this)
        val dao = database.taskDao()
        val viewModel = ViewModelProvider(this, HomeScreenViewModelFactory(dao))[HomeScreenViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            TaskManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel
                    )
                }
            }
        }
    }
}