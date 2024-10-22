package com.erikmobiledev.simpletask.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.erikmobiledev.simpletask.presentation.data.Task
import com.erikmobiledev.simpletask.R

@OptIn(ExperimentalMaterialApi::class) // Habilitar la API experimental
@Composable
fun TaskComponent(
    task: Task, onDismiss: () -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }

    val dismissState = rememberDismissState(confirmStateChange = {
        if (it == DismissValue.DismissedToStart) {
            onDismiss() // Llama a la función de eliminación
        }
        true // Siempre retorna verdadero para indicar que se aceptó el cambio
    })

    SwipeToDismiss(state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.eliminar),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        },
        dismissContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(painter = painterResource(id = if (isChecked) R.drawable.icon_circle_check else R.drawable.icon_circle),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { isChecked = !isChecked },
                        tint = if (isChecked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary)
                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = task.title,
                            fontWeight = FontWeight.Bold,
                            color = if (isChecked) MaterialTheme.colorScheme.tertiary else Color.Unspecified,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else null // Tachar el texto si está marcado
                        )
                        Row {
                            if (task.firstHour != "") {
                                Text(
                                    text = task.firstHour,
                                    color = if (isChecked) MaterialTheme.colorScheme.tertiary else Color.Unspecified,
                                    textDecoration = if (isChecked) TextDecoration.LineThrough else null // Tachar el texto si está marcado
                                )
                            }

                            if (task.secondHour != "" && task.firstHour != "") {
                                task.secondHour?.let {
                                    Text(
                                        text = " - $it",
                                        color = if (isChecked) MaterialTheme.colorScheme.tertiary else Color.Unspecified,
                                        textDecoration = if (isChecked) TextDecoration.LineThrough else null // Tachar el texto si está marcado
                                    )
                                }
                            }
                        }
                    }
                }
            }
        })
}
