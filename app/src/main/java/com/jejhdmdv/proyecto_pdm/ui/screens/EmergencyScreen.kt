package com.jejhdmdv.proyecto_pdm.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.R
import com.jejhdmdv.proyecto_pdm.ui.components.BottomNavBar

/*
  Data class para representar la información de una clínica veterinaria de emergencia.
 */
data class EmergencyClinicInfo(
    val clinicImage: Painter? = null,
    val address: String,
    val phone: String,
    val hours: String
)

//pantalla de emrgencia

@Composable
fun EmergencyScreen(
    onNavigateToAppointments: () -> Unit = {},
    onNavigateToStore: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onCallEmergency: () -> Unit = {},
    modifier: Modifier = Modifier,
    // Lista de información de clínicas, para ser dinámica
    clinicInfoList: List<EmergencyClinicInfo> = emptyList()
) {
    Scaffold(
        bottomBar = {
            // Barra de navegación inferior
            BottomNavBar(
                currentRoute = "emergency",
                onNavigateToHome = {  },
                onNavigateToEmergency = {  },
                onNavigateToAppointments = onNavigateToAppointments,
                onNavigateToStore = onNavigateToStore,
                onNavigateToSettings = onNavigateToSettings
            )
        }
    ) { paddingValues ->
        // Contenedor principal
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // titulo
                EmergencyTitleSection()

                Spacer(modifier = Modifier.height(40.dp))

                // Iterar sobre la lista de clínicas para mostrar las tarjetas
                if (clinicInfoList.isEmpty()) {
                    //mostrar sino hay datos
                    Text(
                        text = "No hay información de clínicas de emergencia disponible.",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(20.dp)
                    )
                } else {
                    clinicInfoList.forEachIndexed { index, clinicInfo ->
                        EmergencyInfoCard(
                            clinicImage = clinicInfo.clinicImage,
                            address = clinicInfo.address,
                            phone = clinicInfo.phone,
                            hours = clinicInfo.hours,
                            onCallEmergency = onCallEmergency
                        )
                        if (index < clinicInfoList.lastIndex) {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}


@Composable
private fun EmergencyTitleSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "EMERGENCIA",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF73A5A7),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.width(10.dp))

        Image(
            painter = painterResource(id = R.drawable.emergency_paw_icon),
            contentDescription = "Emergency Icon",
            modifier = Modifier.size(40.dp)
        )
    }
}


 //Card principal con información de contacto de emergencia

@Composable
private fun EmergencyInfoCard(
    clinicImage: Painter?,
    address: String,
    phone: String,
    hours: String,
    onCallEmergency: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Imagen de la clínica veterinaria
            if (clinicImage != null) {
                Image(
                    painter = clinicImage,
                    contentDescription = "Veterinary Clinic Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // visual si no hay imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Imagen de Clínica", color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Información de contacto
            ContactInfoSection(
                address = address,
                phone = phone,
                hours = hours
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón de llamada de emergencia
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                EmergencyCallButton(onClick = onCallEmergency)
            }

            //EmergencyCallButton(onClick = onCallEmergency)
        }
    }
}


//  Sección de información de contacto

@Composable
private fun ContactInfoSection(
    address: String,
    phone: String,
    hours: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Información de dirección
        ContactInfoItem(
            icon = Icons.Default.LocationOn,
            label = "Dirección",
            value = address
        )

        // Información de teléfono
        ContactInfoItem(
            icon = Icons.Default.Call,
            label = "Teléfono",
            value = phone
        )

        // Información de horario
        ContactInfoItem(
            icon = Icons.Default.Schedule,
            label = "Horarios",
            value = hours
        )
    }
}


  //Elemento individual de información de contacto

@Composable
private fun ContactInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = Color.Gray
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = value,
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 18.sp
            )
        }
    }
}


 //Botón de llamada de emergencia

@Composable
private fun EmergencyCallButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF73A5A7),
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Llamar",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmergencyScreenPreview() {
    EmergencyScreen(
        clinicInfoList = listOf(
            EmergencyClinicInfo(
                clinicImage = null,
                address = "Dirección: [Dirección de la clínica 1]",
                phone = "Teléfono: [Teléfono de la clínica 1]",
                hours = "Horarios: [Horarios de la clínica 1]"
            ),
            EmergencyClinicInfo(
                clinicImage = null,
                address = "Dirección: [Dirección de la clínica 2]",
                phone = "Teléfono: [Teléfono de la clínica 2]",
                hours = "Horarios: [Horarios de la clínica 2]"
            )
        )
    )
}
