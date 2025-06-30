package com.jejhdmdv.proyecto_pdm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.calendarioviewmodel.ReminderUiState
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.calendarioviewmodel.ReminderViewModel
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun ReminderScreen(viewModel: ReminderViewModel = viewModel()) {

    // Recolectar el flujo de mensajes para mostrar Toasts
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.userMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Recolectar el estado principal de la UI
    val uiStateState = viewModel.uiState.collectAsState()
    val uiState = uiStateState.value

    val remindersForDayState = viewModel.remindersForSelectedDate.collectAsState()
    val remindersForDay = remindersForDayState.value

    Column {
        // Aquí iría tu carrusel de fechas, que llama a viewModel.setSelectedDate(newDate)

        when (val state = uiState) {
            is ReminderUiState.Loading -> {
                // Muestra un indicador de carga
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            is ReminderUiState.Success -> {
                // Muestra la lista de recordatorios para el día
                LazyColumn {
                    items(remindersForDay) { event ->
                        Text(text = event.summary ?: "Sin título")
                    }
                }
            }
            is ReminderUiState.Error -> {
                // Muestra un mensaje de error
                Text(text = state.message, color = Color.Red)
            }
        }

        // Botón para añadir un nuevo recordatorio
        Button(onClick = {
            viewModel.addReminder(
                title = "Pasear al perro",
                description = "Dar una vuelta por el parque.",
                date = LocalDate.now(),
                time = LocalTime.of(18, 0)
            )
        }) {
            Text("Añadir Recordatorio")
        }
    }
}