package com.jejhdmdv.proyecto_pdm.ui.viewmodels.calendarioviewmodel

import com.google.api.services.calendar.model.Event
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.CalendarScopes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Collections
import java.util.Date

sealed interface ReminderUiState {
    data object Loading : ReminderUiState
    data class Success(val events: List<Event>) : ReminderUiState
    data class Error(val message: String) : ReminderUiState
}

class ReminderViewModel : androidx.lifecycle.ViewModel() {

    private var calendarService: com.google.api.services.calendar.Calendar? = null

    // Estado de la cuenta de google
    private val _googleAccount = MutableStateFlow<GoogleSignInAccount?>(null)
    val googleAccount: StateFlow<GoogleSignInAccount?> = _googleAccount

    // Estado  de la UI (cargando, exito error)
    private val _uiState = MutableStateFlow<ReminderUiState>(ReminderUiState.Success(emptyList()))
    val uiState: StateFlow<ReminderUiState> = _uiState

    // Día seleccionado
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    // Flujo para enviar mensajes para toasts/snacks
    private val _userMessage = MutableSharedFlow<String>()
    val userMessage: SharedFlow<String> = _userMessage

    // Se recalcula cuando _uiState o _selectedDate cambian.
    val remindersForSelectedDate: StateFlow<List<Event>> =
        combine(uiState, selectedDate) { state, date ->
            if (state is ReminderUiState.Success) {
                state.events.filter { event ->
                    isEventOnDate(event, date)
                }
            } else {
                emptyList()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    fun setGoogleAccount(account: GoogleSignInAccount, context: android.content.Context) {
        _googleAccount.value = account
        val credential = GoogleAccountCredential.usingOAuth2(context, Collections.singleton(CalendarScopes.CALENDAR))
        credential.selectedAccount = account.account
        calendarService = com.google.api.services.calendar.Calendar.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            credential
        ).setApplicationName("PetCare").build()

        // Cargar los recordatorios iniciales al configurar la cuenta
        loadReminders()
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun loadReminders() {
        val service = calendarService ?: return
        _uiState.value = ReminderUiState.Loading

        viewModelScope.launch {
            try {
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

    fun addReminder(title: String, description: String, date: LocalDate, time: LocalTime) {
        val service = calendarService ?: return

        viewModelScope.launch {
            try {
                val createdEvent = withContext(Dispatchers.IO) {
                    val startDateTime = DateTime(Date.from(date.atTime(time).atZone(ZoneId.systemDefault()).toInstant()))
                    val endDateTime = DateTime(Date.from(date.atTime(time).plusHours(1).atZone(ZoneId.systemDefault()).toInstant()))

                    val event = Event()
                        .setSummary(title)
                        .setDescription(description)
                        .setStart(com.google.api.services.calendar.model.EventDateTime().setDateTime(startDateTime))
                        .setEnd(com.google.api.services.calendar.model.EventDateTime().setDateTime(endDateTime))

                    service.events().insert("primary", event).execute()
                }
                _userMessage.emit("Recordatorio '${createdEvent.summary}' añadido.")
                // Recargar la lista para reflejar nuevo estado
                loadReminders()
            } catch (e: Exception) {
                e.printStackTrace()
                _userMessage.emit("Error al añadir recordatorio: ${e.message}")
            }
        }
    }

    private fun isEventOnDate(event: Event, date: LocalDate): Boolean {
        // El evento tiene fecha y hora (dateTime)
        val eventDateTime = event.start?.dateTime
        if (eventDateTime != null) {
            val eventLocalDate = Instant.ofEpochMilli(eventDateTime.value)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            return eventLocalDate == date
        }

        //El evento es de todo el día (date)
        val eventDate = event.start?.date
        if (eventDate != null) {
            // La API devuelve el formato 'YYYY-MM-DD'
            return LocalDate.parse(eventDate.toString()) == date
        }

        return false
    }
}


