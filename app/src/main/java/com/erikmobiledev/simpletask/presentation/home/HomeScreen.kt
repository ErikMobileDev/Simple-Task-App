package com.erikmobiledev.simpletask.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erikmobiledev.simpletask.R
import com.erikmobiledev.simpletask.components.TaskComponent

@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val tasks by viewModel.tasks.observeAsState(emptyList())
    var showDialog by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }
    var newFirstHour by remember { mutableStateOf("") }
    var newSecondHour by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Text(
                text = stringResource(R.string.tareas),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        ) {
            LazyColumn {
                items(tasks, key = { it.id }) { task -> // Usa el ID como clave
                    TaskComponent(task = task, onDismiss = { viewModel.removeTask(task) })
                }
            }
        }

        Box(modifier = Modifier.clickable {
            showDialog = true
        }) {
            Row(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_add),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.nueva_tarea),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Diálogo
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false }, // Cierra el diálogo al tocar fuera
                title = { Text(stringResource(R.string.agregar_nueva_tarea)) },
                text = {
                    Column {
                        TextField(
                            value = newTaskTitle,
                            onValueChange = {
                                newTaskTitle = it
                            }, // Actualiza el título de la tarea
                            label = { Text(stringResource(R.string.descripci_n_de_la_tarea)) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = newFirstHour,
                            onValueChange = { newFirstHour = it }, // Actualiza la hora de inicio
                            label = { Text(stringResource(R.string.hora_de_inicio_opcional)) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = newSecondHour,
                            onValueChange = { newSecondHour = it }, // Actualiza la hora de fin
                            label = { Text(stringResource(R.string.hora_de_fin_opcional)) }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Lógica para confirmar y agregar la tarea
                            viewModel.addTask(newTaskTitle, newFirstHour, newSecondHour)
                            showDialog = false // Cierra el diálogo
                            // Limpia los campos después de agregar la tarea
                            newTaskTitle = ""
                            newFirstHour = ""
                            newSecondHour = ""
                        },
                        enabled = newTaskTitle.isNotEmpty() // Habilita el botón solo si el título no está vacío
                    ) {
                        Text(stringResource(R.string.confirmar))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                        // Limpia los campos si se cancela
                        newTaskTitle = ""
                        newFirstHour = ""
                        newSecondHour = ""
                    }) {
                        Text(stringResource(R.string.cancelar))
                    }
                }
            )
        }
    }
}
