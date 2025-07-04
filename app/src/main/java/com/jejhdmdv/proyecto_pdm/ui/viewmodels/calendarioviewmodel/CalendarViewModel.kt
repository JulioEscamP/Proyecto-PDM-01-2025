package com.jejhdmdv.proyecto_pdm.ui.viewmodels.calendarioviewmodel

import com.google.api.services.calendar.model.Event
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.CalendarScopes
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.EventDateTime
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.util.Collections
import java.util.Date

sealed interface ReminderUiState {
    data object Loading : ReminderUiState
    data class Success(val events: List<Event>) : ReminderUiState
    data class Error(val message: String) : ReminderUiState
}

data class CalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate? = null,
    val selectedTime: String? = null,
    val eventsForMonth: List<Event> = emptyList(),
    val isLoading: Boolean = false,
    val appointmentResult: Resource<Event> = Resource.Idle()
)

class ReminderViewModel : ViewModel() {

    private var calendarService: Calendar? = null

    // Estado de la cuenta de google
    // private val _googleAccount = MutableStateFlow<GoogleSignInAccount?>(null)
    // val googleAccount: StateFlow<GoogleSignInAccount?> = _googleAccount

    // Estado  de la UI (cargando, exito error)
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _appointmentResult = MutableStateFlow<Resource<Event>>(Resource.Idle())
    val appointmentResult: StateFlow<Resource<Event>> = _appointmentResult.asStateFlow()

    fun initialize(account: GoogleSignInAccount, context: android.content.Context) {
        if (calendarService != null) return // Evitar reinicializar

        // --- Lógica de Inicialización ---
        val credential = GoogleAccountCredential.usingOAuth2(context, setOf(CalendarScopes.CALENDAR))
        credential.selectedAccount = account.account
        calendarService = Calendar.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).setApplicationName("Proyecto PDM").build()

        // Cargar los eventos para el mes actual al inicializar
        loadEventsForMonth(YearMonth.now())
    }

    // --- Manejo de Acciones de la UI ---
    fun onMonthChange(newMonth: YearMonth) {
        _uiState.update { it.copy(currentMonth = newMonth, selectedDate = null, selectedTime = null) }
        loadEventsForMonth(newMonth)
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date, selectedTime = null) }
    }

    fun onTimeSelected(time: String) {
        _uiState.update { it.copy(selectedTime = time) }
    }

    //Funcion de calendario con Google API.
    private fun loadEventsForMonth(month: YearMonth) {
        val service = calendarService ?: return
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val firstDay = month.atDay(1).atStartOfDay(ZoneId.systemDefault())
                    val lastDay = month.atEndOfMonth().atTime(23, 59, 59).atZone(ZoneId.systemDefault())

                    val events = service.events().list("primary")
                        .setTimeMin(DateTime(firstDay.toInstant().toEpochMilli()))
                        .setTimeMax(DateTime(lastDay.toInstant().toEpochMilli()))
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute()
                    Resource.Success(events.items ?: emptyList())
                } catch (e: Exception) {
                    Resource.Error("Error al cargar eventos: ${e.message}")
                }
            }

            if (result is Resource.Success) {
                _uiState.update { it.copy(eventsForMonth = result.data ?: emptyList(), isLoading = false) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
                // TODO: Manejar el estado de error, por ejemplo, mostrando un Toast
            }
        }
    }

    fun confirmAppointment() {
        val service = calendarService ?: return
        val state = _uiState.value
        val date = state.selectedDate ?: return
        val time = state.selectedTime ?: return

        _uiState.update { it.copy(appointmentResult = Resource.Loading()) }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                try {
                    val (hour, minute) = time.split(":").map { it.toInt() }
                    val startDateTime = date.atTime(hour, minute)
                    val endDateTime = startDateTime.plusHours(1) // Asumimos citas de 1 hora

                    val event = Event().apply {
                        summary = "Cita Agendada - PetCare"
                        description = "Cita agendada a través de la aplicación."
                        start = EventDateTime().setDateTime(
                            DateTime(startDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                        )
                        end = EventDateTime().setDateTime(
                            DateTime(endDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                        )
                    }
                    val createdEvent = service.events().insert("primary", event).execute()
                    Resource.Success(createdEvent)
                } catch (e: Exception) {
                    Resource.Error("Error al crear la cita: ${e.message}")
                }
            }
            _uiState.update { it.copy(appointmentResult = result) }
        }
    }

    fun resetAppointmentResult() {
        _uiState.update { it.copy(appointmentResult = Resource.Idle()) }
    }
}


