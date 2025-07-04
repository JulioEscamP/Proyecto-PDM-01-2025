package com.jejhdmdv.proyecto_pdm.ui.admin.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.model.admin.Clinic
import com.jejhdmdv.proyecto_pdm.model.admin.ClinicRequest
import androidx.compose.ui.tooling.preview.Preview

//añadir o editar clinicas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminClinicFormScreen(
    clinic: Clinic? = null,
    isLoading: Boolean,
    onNavigateBack: () -> Unit,
    onSaveClinic: (ClinicRequest) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue(clinic?.name ?: "")) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue(clinic?.phoneNumber ?: "")) }
    var address by remember { mutableStateOf(TextFieldValue(clinic?.address ?: "")) }
    var schedule by remember { mutableStateOf(TextFieldValue(clinic?.schedule ?: "")) }
    var isActive by remember { mutableStateOf(clinic?.isActive ?: true) }

    var nameError by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf("") }
    var addressError by remember { mutableStateOf("") }
    var scheduleError by remember { mutableStateOf("") }

    val isEditing = clinic != null

    fun validateForm(): Boolean {
        nameError = if (name.text.isBlank()) "El nombre es requerido" else ""
        phoneError = if (phoneNumber.text.isBlank()) "El teléfono es requerido" else ""
        addressError = if (address.text.isBlank()) "La dirección es requerida" else ""
        scheduleError = if (schedule.text.isBlank()) "El horario es requerido" else ""

        return nameError.isEmpty() && phoneError.isEmpty() && addressError.isEmpty() && scheduleError.isEmpty()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text(if (isEditing) "Editar Clínica" else "Nueva Clínica") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información de la Clínica",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Campo Nombre
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = ""
                        },
                        label = { Text("Nombre de la clínica *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = nameError.isNotEmpty(),
                        supportingText = if (nameError.isNotEmpty()) {
                            { Text(nameError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        leadingIcon = {
                            Icon(Icons.Default.LocalHospital, contentDescription = null)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Teléfono
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it
                            phoneError = ""
                        },
                        label = { Text("Número de teléfono *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = phoneError.isNotEmpty(),
                        supportingText = if (phoneError.isNotEmpty()) {
                            { Text(phoneError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Dirección
                    OutlinedTextField(
                        value = address,
                        onValueChange = {
                            address = it
                            addressError = ""
                        },
                        label = { Text("Dirección completa *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = addressError.isNotEmpty(),
                        supportingText = if (addressError.isNotEmpty()) {
                            { Text(addressError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null)
                        },
                        minLines = 2,
                        maxLines = 3,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Horario
                    OutlinedTextField(
                        value = schedule,
                        onValueChange = {
                            schedule = it
                            scheduleError = ""
                        },
                        label = { Text("Horario de atención *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = scheduleError.isNotEmpty(),
                        supportingText = if (scheduleError.isNotEmpty()) {
                            { Text(scheduleError, color = MaterialTheme.colorScheme.error) }
                        } else {
                            { Text("Ejemplo: Lunes a Viernes 8:00 AM - 6:00 PM") }
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Schedule, contentDescription = null)
                        },
                        minLines = 2,
                        maxLines = 3,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Switch Estado Activo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Clínica activa",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Switch(
                            checked = isActive,
                            onCheckedChange = { isActive = it }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Guardar
            Button(
                onClick = {
                    if (validateForm()) {
                        val clinicRequest = ClinicRequest(
                            name = name.text.trim(),
                            phoneNumber = phoneNumber.text.trim(),
                            address = address.text.trim(),
                            schedule = schedule.text.trim(),
                            isActive = isActive
                        )
                        onSaveClinic(clinicRequest)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEditing) "Actualizar Clínica" else "Crear Clínica",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = "* Campos requeridos",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AdminClinicFormScreenPreview() {
    AdminClinicFormScreen(
        isLoading = false,
        onNavigateBack = {},
        onSaveClinic = {}
    )
}
