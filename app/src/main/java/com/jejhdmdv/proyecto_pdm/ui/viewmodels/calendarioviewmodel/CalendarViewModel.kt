package com.jejhdmdv.proyecto_pdm.ui.viewmodels.calendarioviewmodel

import com.google.api.services.calendar.model.Event
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.CalendarScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Collections
import java.util.Date

sealed interface ReminderUiState {
    data object Loading : ReminderUiState
    data class Success(val events: List<Event>) : ReminderUiState
    data class Error(val message: String) : ReminderUiState
}

class ReminderViewModel : androidx.lifecycle.ViewModel() {

    // Para manejar el estado de la autenticación de Google Calendar

    private val _googleAccount = mutableStateOf<GoogleSignInAccount?>(null)
    val googleAccount: State<GoogleSignInAccount?> = _googleAccount

    // Estado principal de la UI (Cargando, Éxito con datos, Error)
    private val _uiState = MutableStateFlow<ReminderUiState>(ReminderUiState.Success(emptyList()))
    val uiState: StateFlow<ReminderUiState> = _uiState

    // Init instancia del cliente de Google Calendar (se inicializa una vez autenticado)

    var calendarService: com.google.api.services.calendar.Calendar? = null
        private set

    // Estado para los recordatorios cargados para el mes/día

    private val _reminders = mutableStateListOf<com.google.api.services.calendar.model.Event>()
    val reminders: List<com.google.api.services.calendar.model.Event> = _reminders

    // Día seleccionado en el carrusel

    private val _selectedDate = mutableStateOf(LocalDate.now())
    val selectedDate: State<LocalDate> = _selectedDate

    // Recordatorios del día seleccionado

    val remindersForSelectedDate: List<com.google.api.services.calendar.model.Event>
        get() = _reminders.filter { event ->

            // Para eventos con fecha y hora específicas (EventDateTime.getDateTime())

            val eventDateTime = event.start?.dateTime
            val isDateTimeMatch = eventDateTime != null &&
                    Instant.ofEpochMilli(eventDateTime.value)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate() == _selectedDate.value


            val eventDate = event.start?.getDate()
            val isDateMatch =
                eventDate != null && eventDate.toString() == _selectedDate.value.format(
                    DateTimeFormatter.ISO_DATE
                )

            isDateTimeMatch || isDateMatch
        }

    fun setSelectedDate(date: LocalDate) {

        _selectedDate.value = date

    }

    fun setGoogleAccount(account: GoogleSignInAccount, context: Context) {
        _googleAccount.value = account

        // Inicializar el servicio de Calendar

        val credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(CalendarScopes.CALENDAR))
        credential.selectedAccount = account.account
        calendarService = com.google.api.services.calendar.Calendar.Builder(
            NetHttpTransport(),
            com.google.api.client.json.gson.GsonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName("PetCare")

            .build()
    }



    // Función para cargar recordatorios desde Google Calendar
    fun loadReminders() {
        val service = calendarService ?: return
        _uiState.value = ReminderUiState.Loading

        viewModelScope.launch {
            try {
                //CORUTINA - CAMBIO DE PROCESO A RUTINA SECUNDARIA
                val events = withContext(Dispatchers.IO) {
                    val now = DateTime(System.currentTimeMillis())
                    service.events().list("primary")
                        .setTimeMin(now)
                        .setSingleEvents(true)
                        .setOrderBy("startTime")
                        .execute()
                }
                _uiState.value = ReminderUiState.Success(events.items ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = ReminderUiState.Error("Error al cargar recordatorios: ${e.message}")
            }
        }
    }

    // Función para añadir un recordatorio (GOOGLE API SERVICE)

    fun addReminder(context: Context, title: String, description: String, date: LocalDate, time: java.time.LocalTime) {
        val service = calendarService
        if (service != null) {
            viewModelScope.launch {
                try {
                    val localDateTime = date.atTime(time)
                    val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
                    val startDateTime = DateTime(Date.from(instant))

                    val endLocalDateTime = date.atTime(time).plusHours(1)
                    val endInstant = endLocalDateTime.atZone(ZoneId.systemDefault()).toInstant()
                    val endDateTime = DateTime(Date.from(endInstant))

                    val event = com.google.api.services.calendar.model.Event()
                        .setSummary(title)
                        .setDescription(description)
                        .setStart(com.google.api.services.calendar.model.EventDateTime().setDateTime(startDateTime))
                        .setEnd(com.google.api.services.calendar.model.EventDateTime().setDateTime(endDateTime))

                    val createdEvent = service.events().insert("primary", event).execute()
                    _reminders.add(createdEvent)
                    Toast.makeText(context, "Recordatorio '${createdEvent.summary}' añadido.", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    Toast.makeText(context, "Error al añadir recordatorio: ${e.message}", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }
            }
        }
    }
}