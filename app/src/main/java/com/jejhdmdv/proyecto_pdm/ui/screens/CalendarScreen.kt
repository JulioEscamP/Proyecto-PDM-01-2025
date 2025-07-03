package com.jejhdmdv.proyecto_pdm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.calendarioviewmodel.CalendarUiState
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    uiState: CalendarUiState, // Recibe el estado completo
    onNavigateBack: () -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onTimeSelected: (String) -> Unit,
    onConfirmAppointment: () -> Unit,
    unavailableDates: List<LocalDate>,
    availableTimeSlots: List<String>,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Título principal
            Text(
                text = "Agendar cita",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF73A5A7),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sección del calendario
            CalendarSection(
                currentMonth = uiState.currentMonth,
                selectedDate = uiState.selectedDate,
                onMonthChange = onMonthChange,
                onDateSelected = onDateSelected,
                unavailableDates = unavailableDates
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sección de selección de hora
            TimeSelectionSection(
                selectedTime = uiState.selectedTime,
                onTimeSelected = onTimeSelected,
                timeSlots = availableTimeSlots
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón confirmar
            ConfirmButton(
                isEnabled = uiState.selectedDate != null && uiState.selectedTime != null,
                onClick = onConfirmAppointment
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

//seccion para el calendario, incluye navegacion del mes
@Composable
private fun CalendarSection(
    currentMonth: YearMonth,
    selectedDate: LocalDate?,
    onMonthChange: (YearMonth) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    unavailableDates: List<LocalDate>
) {
    Column {
        // Header del mes con navegación
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Mes anterior",
                    tint = Color(0xFF73A5A7)
                )
            }

            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale("es"))} ${currentMonth.year}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF73A5A7)
            )

            IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Mes siguiente",
                    tint = Color(0xFF73A5A7)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Días de la semana
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val daysOfWeek = listOf("LU", "MA", "MI", "JU", "VI", "SA", "DO")
            daysOfWeek.forEach { day ->
                Text(
                    text = day,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Grid del calendario
        CalendarGrid(
            currentMonth = currentMonth,
            selectedDate = selectedDate,
            onDateSelected = onDateSelected,
            unavailableDates = unavailableDates
        )
    }
}


 // Grid del calendario con los días del mes

@Composable
private fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    unavailableDates: List<LocalDate>
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    val lastDayOfMonth = currentMonth.atEndOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val daysInMonth = currentMonth.lengthOfMonth()

    val days = mutableListOf<LocalDate?>()

    // Días vacíos al inicio
    repeat(firstDayOfWeek) {
        days.add(null)
    }

    // Días del mes
    for (day in 1..daysInMonth) {
        days.add(currentMonth.atDay(day))
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.height(240.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(days) { date ->
            val isAvailable = date?.let { it !in unavailableDates } ?: false
            CalendarDay(
                date = date,
                isSelected = date == selectedDate,
                isToday = date == LocalDate.now(),
                isAvailable = isAvailable,
                onClick = { date?.let { onDateSelected(it) } }
            )
        }
    }
}

//componente apra cada dia del calendario
@Composable
private fun CalendarDay(
    date: LocalDate?,
    isSelected: Boolean,
    isToday: Boolean,
    isAvailable: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(
                when {
                    isSelected -> Color(0xFF73A5A7)
                    isToday -> Color(0xFF73A5A7).copy(alpha = 0.3f)
                    !isAvailable -> Color.LightGray.copy(alpha = 0.5f)
                    else -> Color.Transparent
                }
            )
            .clickable(enabled = date != null && isAvailable) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        date?.let {
            Text(
                text = it.dayOfMonth.toString(),
                fontSize = 14.sp,
                color = when {
                    isSelected -> Color.White
                    isToday -> Color(0xFF73A5A7)
                    !isAvailable -> Color.Gray
                    else -> Color.Black
                },
                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

//seleccion de hora

@Composable
private fun TimeSelectionSection(
    selectedTime: String?,
    onTimeSelected: (String) -> Unit,
    timeSlots: List<String>
) {
    Column {
        Text(
            text = "Selecciona la hora",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF73A5A7),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Grid de horarios disponibles
        val timeSlots = listOf(
            "8:00 a.m.", "10:00 a.m.",
            "11:00 a.m.", "2:00 p.m.",
            "4:00 p.m.", "6:00 p.m."
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(180.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(timeSlots) { time ->
                TimeSlotButton(
                    time = time,
                    isSelected = selectedTime == time,
                    onClick = { onTimeSelected(time) }
                )
            }
        }
    }
}

//botones para cada horario

@Composable
private fun TimeSlotButton(
    time: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF73A5A7) else Color.White,
            contentColor = if (isSelected) Color.White else Color(0xFF73A5A7)
        ),
        shape = RoundedCornerShape(8.dp),
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(
            1.dp,
            Color(0xFF73A5A7)
        ) else null
    ) {
        Text(
            text = time,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

//boton para confirmar citas

@Composable
private fun ConfirmButton(
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF73A5A7),
            contentColor = Color.White,
            disabledContainerColor = Color.Gray.copy(alpha = 0.6f),
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Confirmar",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}