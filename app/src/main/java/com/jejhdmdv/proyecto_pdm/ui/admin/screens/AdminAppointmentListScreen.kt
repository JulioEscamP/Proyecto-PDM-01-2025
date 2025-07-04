package com.jejhdmdv.proyecto_pdm.ui.admin.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.model.admin.Appointment
import com.jejhdmdv.proyecto_pdm.model.admin.AppointmentStatus
import androidx.compose.ui.tooling.preview.Preview

//gestion de citas para el administrador

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAppointmentListScreen(
    appointments: List<Appointment>,
    isLoading: Boolean,
    onNavigateBack: () -> Unit,
    onApproveAppointment: (String, String) -> Unit,
    onRejectAppointment: (String, String) -> Unit,
    onRefresh: () -> Unit
) {
    var showActionDialog by remember { mutableStateOf(false) }
    var selectedAppointment by remember { mutableStateOf<Appointment?>(null) }
    var actionType by remember { mutableStateOf<String?>(null) }
    var adminNotes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Gestionar Citas") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            },
            actions = {
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                }
            }
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val pendingCount = appointments.count { it.status == AppointmentStatus.PENDING }
                    val approvedCount = appointments.count { it.status == AppointmentStatus.APPROVED }
                    val rejectedCount = appointments.count { it.status == AppointmentStatus.REJECTED }

                    StatCard(
                        title = "Pendientes",
                        count = pendingCount,
                        color = Color(0xFFFF9800),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Aprobadas",
                        count = approvedCount,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Rechazadas",
                        count = rejectedCount,
                        color = Color(0xFFF44336),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de citas
                if (appointments.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No hay citas registradas",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(appointments) { appointment ->
                            AppointmentListItem(
                                appointment = appointment,
                                onApprove = {
                                    selectedAppointment = appointment
                                    actionType = "approve"
                                    showActionDialog = true
                                },
                                onReject = {
                                    selectedAppointment = appointment
                                    actionType = "reject"
                                    showActionDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Diálogo para aprobar/rechazar cita
    if (showActionDialog && selectedAppointment != null && actionType != null) {
        AlertDialog(
            onDismissRequest = {
                showActionDialog = false
                selectedAppointment = null
                actionType = null
                adminNotes = ""
            },
            title = {
                Text(if (actionType == "approve") "Aprobar Cita" else "Rechazar Cita")
            },
            text = {
                Column {
                    Text("¿Estás seguro de que quieres ${if (actionType == "approve") "aprobar" else "rechazar"} esta cita?")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = adminNotes,
                        onValueChange = { adminNotes = it },
                        label = { Text("Notas (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedAppointment?.let { appointment ->
                            if (actionType == "approve") {
                                onApproveAppointment(appointment.id, adminNotes)
                            } else {
                                onRejectAppointment(appointment.id, adminNotes)
                            }
                        }
                        showActionDialog = false
                        selectedAppointment = null
                        actionType = null
                        adminNotes = ""
                    }
                ) {
                    Text(
                        if (actionType == "approve") "Aprobar" else "Rechazar",
                        color = if (actionType == "approve") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showActionDialog = false
                        selectedAppointment = null
                        actionType = null
                        adminNotes = ""
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@Composable
private fun StatCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(80.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = color
            )
        }
    }
}

//item de citas a la vista
@Composable
private fun AppointmentListItem(
    appointment: Appointment,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = appointment.userName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .background(
                            when (appointment.status) {
                                AppointmentStatus.PENDING -> Color(0xFFFF9800)
                                AppointmentStatus.APPROVED -> Color(0xFF4CAF50)
                                AppointmentStatus.REJECTED -> Color(0xFFF44336)
                                else -> Color.Gray
                            }.copy(alpha = 0.2f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (appointment.status) {
                            AppointmentStatus.PENDING -> "Pendiente"
                            AppointmentStatus.APPROVED -> "Aprobada"
                            AppointmentStatus.REJECTED -> "Rechazada"
                            else -> "Desconocido"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (appointment.status) {
                            AppointmentStatus.PENDING -> Color(0xFFFF9800)
                            AppointmentStatus.APPROVED -> Color(0xFF4CAF50)
                            AppointmentStatus.REJECTED -> Color(0xFFF44336)
                            else -> Color.Gray
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Información de la cita
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${appointment.petName} (${appointment.petType})",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${appointment.appointmentDate} - ${appointment.appointmentTime}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (appointment.reason.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Motivo: ${appointment.reason}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (appointment.adminNotes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Notas del admin: ${appointment.adminNotes}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Botones de acción solo para citas pendientes
            if (appointment.status == AppointmentStatus.PENDING) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Rechazar")
                    }

                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Aprobar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminAppointmentListScreenPreview() {
    AdminAppointmentListScreen(
        appointments = listOf(
            Appointment(
                id = "1",
                userName = "michell melendez ",
                petName = "pepito",
                petType = "Perro",
                appointmentDate = "2025-03-10",
                appointmentTime = "10:00",
                reason = "Vacunación",
                status = AppointmentStatus.PENDING,
                adminNotes = "un perrito muy guapo"
            ),
            Appointment(
                id = "2",
                userName = "Pedro Torres",
                petName = "pelusa",
                petType = "Gato",
                appointmentDate = "2025-03-09",
                appointmentTime = "14:30",
                reason = "Revisión general",
                status = AppointmentStatus.APPROVED,
                adminNotes = "Todo en orden."
            )
        ),
        isLoading = false,
        onNavigateBack = {},
        onApproveAppointment = { _, _ -> },
        onRejectAppointment = { _, _ -> },
        onRefresh = {}
    )
}
