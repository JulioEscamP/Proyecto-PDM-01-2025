package com.jejhdmdv.proyecto_pdm.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.ui.components.BottomNavBar



 // Permite al usuario gestionar su cuenta, preferencias y acceder a información de la app.

@Composable
fun SettingsScreen(
    onNavigateToAppointments: () -> Unit = {},
    onNavigateToEmergency: () -> Unit = {},
    onNavigateToStore: () -> Unit = {},
    onNavigateToFAQ: () -> Unit = {},
    onNavigateToTerms: () -> Unit = {},
    onNavigateToReportProblem: () -> Unit = {},
    onLogout: () -> Unit = {},
    userName: String = "Nombre de Usuario",
    petName: String = "Tu mascota",
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "settings",
                onNavigateToHome = { /* Navegar a home si es necesario */ },
                onNavigateToEmergency = onNavigateToEmergency,
                onNavigateToAppointments = onNavigateToAppointments,
                onNavigateToStore = onNavigateToStore,
                onNavigateToSettings = { /* Ya estamos en ajustes */ }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Sección de perfil de usuario
            UserProfileSection(userName = userName, petName = petName)

            Spacer(modifier = Modifier.height(32.dp))

            // Opciones de menú
            SettingsMenuItem(
                icon = Icons.Default.AccountCircle,
                title = "Mi cuenta",
                onClick = { /* TODO: Implementar navegación a Mi Cuenta */ },
                showArrow = true
            )
            SettingsMenuItem(
                icon = Icons.Default.History,
                title = "Historial médico",
                onClick = { /* TODO: Implementar navegación a Historial Médico */ },
                showPlus = true
            )
            SettingsMenuItem(
                icon = Icons.Default.Info,
                title = "Gestión de citas",
                onClick = onNavigateToAppointments
            )
            SettingsMenuItem(
                icon = Icons.Default.Build,
                title = "Preferencias del sistema",
                onClick = { /* TODO: Implementar desplegable de modo claro/oscuro */ },
                showArrow = true
            )
            SettingsMenuItem(
                icon = Icons.Default.QuestionAnswer,
                title = "Preguntas frecuentes",
                onClick = onNavigateToFAQ
            )
            SettingsMenuItem(
                icon = Icons.Default.Info,
                title = "Términos y condiciones",
                onClick = onNavigateToTerms
            )
            SettingsMenuItem(
                icon = Icons.Default.Warning,
                title = "Reportar un problema",
                onClick = onNavigateToReportProblem
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Cerrar Sesión
            SettingsMenuItem(
                icon = Icons.Default.ExitToApp,
                title = "Cerrar Sesión",
                onClick = onLogout
            )
        }
    }
}

/**
 * Sección de perfil de usuario en la parte superior de la pantalla de ajustes.
 */
@Composable
private fun UserProfileSection(
    userName: String,
    petName: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Círculo para la imagen de perfil (placeholder)
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = userName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = petName,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

/**
 * Elemento de menú reutilizable para las opciones de ajustes.
 */
@Composable
private fun SettingsMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    showArrow: Boolean = false,
    showPlus: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(24.dp),
                    tint = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            if (showArrow) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expandir",
                    modifier = Modifier.size(24.dp),
                    tint = Color.DarkGray
                )
            }
        }
        Divider(color = Color.LightGray, thickness = 0.5.dp)
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        userName = "nombre de usuario",
        petName = "tu mascota"
    )
}


