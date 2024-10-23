package com.erikmobiledev.simpletask.presentation.home

import android.annotation.SuppressLint
import android.app.TimePickerDialog
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erikmobiledev.simpletask.R
import com.erikmobiledev.simpletask.components.TaskComponent
import java.util.Calendar

@SuppressLint("DefaultLocale")
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

    // Variables para controlar el estado del TimePicker
    var showFirstHourPicker by remember { mutableStateOf(false) }
    var showSecondHourPicker by remember { mutableStateOf(false) }

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
                            onValueChange = { newTaskTitle = it },
                            label = { Text(stringResource(R.string.descripci_n_de_la_tarea)) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Botón para seleccionar la hora de inicio
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(R.string.hora_de_inicio_opcional), modifier = Modifier.weight(1f))
                            TextButton(onClick = { showFirstHourPicker = true }) {
                                Text(text = newFirstHour.ifEmpty { stringResource(R.string.selecciona_hora) })
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Botón para seleccionar la hora de fin
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = stringResource(R.string.hora_de_fin_opcional), modifier = Modifier.weight(1f))
                            TextButton(onClick = { showSecondHourPicker = true }) {
                                Text(text = newSecondHour.ifEmpty { stringResource(R.string.selecciona_hora) })
                            }
                        }
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

        // Mostrar el TimePicker para la hora de inicio
        if (showFirstHourPicker) {
            TimePickerDialog(
                onDismiss = { showFirstHourPicker = false },
                onTimeSelected = { hour, minute ->
                    newFirstHour = String.format("%02d:%02d", hour, minute)
                    showFirstHourPicker = false
                }
            )
        }

        // Mostrar el TimePicker para la hora de fin
        if (showSecondHourPicker) {
            TimePickerDialog(
                onDismiss = { showSecondHourPicker = false },
                onTimeSelected = { hour, minute ->
                    newSecondHour = String.format("%02d:%02d", hour, minute)
                    showSecondHourPicker = false
                }
            )
        }
    }
}

// Composable para el TimePickerDialog
@Composable
fun TimePickerDialog(onDismiss: () -> Unit, onTimeSelected: (hour: Int, minute: Int) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    // Usar un Dialogo
    val timePickerDialog = remember { TimePickerDialog(context, { _, selectedHour, selectedMinute ->
        onTimeSelected(selectedHour, selectedMinute)
    }, hour, minute, true) }

    // Mostrar el TimePickerDialog
    LaunchedEffect(Unit) {
        timePickerDialog.show()
        onDismiss() // Llamar a onDismiss al cerrar
    }
}
