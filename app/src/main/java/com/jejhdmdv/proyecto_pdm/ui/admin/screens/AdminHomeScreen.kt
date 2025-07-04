package com.jejhdmdv.proyecto_pdm.ui.admin.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*

//pantalla principal para el administrador
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    onNavigateToProducts: () -> Unit,
    onNavigateToClinics: () -> Unit,
    onNavigateToAppointments: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    veterinaryName: String = "Clinica veterinaria\nBienestar Animal"
) {
    // Colores del degradado según la imagen
    val gradientColors = listOf(Color(0xFFCEB6B6), Color(0xFF73A5A7))
    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradientColors))
            .padding(16.dp)
    ) {
        // Header con título, círculo de perfil y menú hamburguesa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Título en la esquina superior izquierda
            Text(
                text = "PET CARE +",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Menú hamburguesa en la esquina superior derecha
            Box(modifier = Modifier.wrapContentSize(Alignment.TopEnd)) {
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = Color.Black
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar Perfil") },
                        onClick = {
                            onNavigateToProfile()
                            showMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Cerrar Sesión") },
                        onClick = {
                            onLogout()
                            showMenu = false
                        }
                    )
                }
            }
        }

        // Círculo de perfil y texto debajo del título
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo de perfil (placeholder para la foto de perfil de la veterinaria)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Texto de la clínica (nombre de la veterinaria registrado)
            Text(
                text = veterinaryName,
                fontSize = 16.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )
        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .weight(1f)
                .padding(top = 24.dp)
        ) {
            items(getAdminMenuItems()) { item ->
                AdminMenuCard(
                    title = item.title,
                    description = item.description,
                    icon = item.icon,
                    backgroundColor = item.backgroundColor,
                    onClick = {
                        when (item.title) {
                            "Gestionar Productos" -> onNavigateToProducts()
                            "Gestionar Clínicas" -> onNavigateToClinics()
                            "Gestionar Citas" -> onNavigateToAppointments()
                            "Mi Perfil" -> onNavigateToProfile()
                        }
                    }
                )
            }
        }
    }
}


@Composable
private fun AdminMenuCard(
    title: String,
    description: String,
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}


private data class AdminMenuItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val backgroundColor: Color
)


private fun getAdminMenuItems(): List<AdminMenuItem> {
    val cardColor = Color(0xFF8BBAC0)

    return listOf(
        AdminMenuItem(
            title = "Gestionar Productos",
            description = "Añadir, editar y eliminar productos de la tienda",
            icon = Icons.Default.ShoppingCart,
            backgroundColor = cardColor
        ),
        AdminMenuItem(
            title = "Gestionar Clínicas",
            description = "Administrar clínicas veterinarias de emergencia",
            icon = Icons.Default.LocalHospital,
            backgroundColor = cardColor
        ),
        AdminMenuItem(
            title = "Gestionar Citas",
            description = "Aprobar o rechazar solicitudes de citas",
            icon = Icons.Default.Schedule,
            backgroundColor = cardColor
        ),
        AdminMenuItem(
            title = "Mi Perfil",
            description = "Editar información personal y configuración",
            icon = Icons.Default.Person,
            backgroundColor = cardColor
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AdminHomeScreenPreview() {
    AdminHomeScreen(
        onNavigateToProducts = {},
        onNavigateToClinics = {},
        onNavigateToAppointments = {},
        onNavigateToProfile = {},
        onLogout = {},
        veterinaryName = "Bienestar Animal"
    )
}
