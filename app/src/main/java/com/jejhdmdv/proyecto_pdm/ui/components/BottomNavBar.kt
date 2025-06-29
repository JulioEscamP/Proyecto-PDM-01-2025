package com.jejhdmdv.proyecto_pdm.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.R

/**
 * Componente de la barra de navegación inferior
 * - Emergencia
 * - Citas
 * - Tienda
 * - Ajustes
 */
@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigateToHome: () -> Unit = {},
    onNavigateToEmergency: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToStore: () -> Unit,
    onNavigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Contenedor principal de la barra de navegación
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emergencia
            BottomNavItem(
                iconRes = R.drawable.emergency_icon,
                label = "Emergencia",
                isSelected = currentRoute == "emergency",
                onClick = onNavigateToEmergency
            )

            // Citas
            BottomNavItem(
                iconRes = R.drawable.appointment_icon,
                label = "Citas",
                isSelected = currentRoute == "appointments",
                onClick = onNavigateToAppointments
            )

            // Tienda
            BottomNavItem(
                iconRes = R.drawable.store_icon,
                label = "Tienda",
                isSelected = currentRoute == "store",
                onClick = onNavigateToStore
            )

            // Ajustes
            BottomNavItem(
                iconRes = R.drawable.report_icon,
                label = "Ajustes",
                isSelected = currentRoute == "settings",
                onClick = onNavigateToSettings
            )
        }
    }
}

/**
 * Elemento individual de la barra de navegación
 * Contiene un icono y una etiqueta de texto
 */
@Composable
private fun BottomNavItem(
    iconRes: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // para el elemento de navegacion
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )

        // Etiqueta de texto
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) Color.Black else Color.Gray
        )
    }
}

