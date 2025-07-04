package com.jejhdmdv.proyecto_pdm.ui.screens

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.R
import com.jejhdmdv.proyecto_pdm.ui.components.BottomNavBar

/**
 * Data class para representar una mascota
 */
data class Pet(
    val id: String,
    val name: String,
    val type: String,
    val imageRes: Int? = null,
    val medicalHistory: List<String> = emptyList()
)

/**
 * Pantalla principal de la aplicación (HomeScreen)
 * Muestra información del usuario, carrusel de mascotas y accesos rápidos
 */
@Composable
fun HomeScreen(
    onNavigateToEmergency: () -> Unit = {},
    onNavigateToAppointments: () -> Unit = {},
    onNavigateToStore: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToPetRegistration: () -> Unit = {},
    modifier: Modifier = Modifier,
    userName: String = "Nombre de Usuario", // Parámetro para el nombre del usuario
    userPets: List<Pet> = samplePets() // Lista de mascotas del usuario
) {
    var selectedPetIndex by remember { mutableStateOf(0) }
    val currentPet = if (userPets.isNotEmpty()) userPets[selectedPetIndex] else null

    Scaffold(
        bottomBar = {
            // Barra de navegación inferior
            BottomNavBar(
                currentRoute = "home",
                onNavigateToHome = {  },
                onNavigateToEmergency = onNavigateToEmergency,
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
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // Sección del usuario
                UserSection(userName = userName, currentPet = currentPet)

                Spacer(modifier = Modifier.height(20.dp))

                // Carrusel de mascotas
                PetCarouselSection(
                    pets = userPets,
                    selectedIndex = selectedPetIndex,
                    onPetSelected = { index -> selectedPetIndex = index }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón de registrar mascota
                RegisterPetButton(onNavigateToPetRegistration = onNavigateToPetRegistration)

                Spacer(modifier = Modifier.height(20.dp))

                // Sección de recordatorios y avisos
                RemindersAndNoticesSection(onNavigateToAppointments = onNavigateToAppointments)

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

/**
 * Sección del usuario con avatar y nombre
 */
@Composable
private fun UserSection(
    userName: String,
    currentPet: Pet?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar del usuario
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = "Avatar del usuario",
                tint = Color.Gray,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = userName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = if (currentPet != null) "Tu mascota: ${currentPet.name}" else "Sin mascotas registradas",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

//seccion de carrusel de la mascota
@Composable
private fun PetCarouselSection(
    pets: List<Pet>,
    selectedIndex: Int,
    onPetSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (expanded) 300.dp else 200.dp)
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            if (pets.isNotEmpty()) {
                val currentPet = pets[selectedIndex]

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(16.dp))

                    currentPet.imageRes?.let { imageResId ->
                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = "Foto de ${currentPet.name}",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    } ?: Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sin foto",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }


                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = currentPet.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = currentPet.type,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    // Historial médico
                    if (expanded && currentPet.medicalHistory.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Historial Médico:",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            currentPet.medicalHistory.forEach { record ->
                                Text(
                                    text = "• $record",
                                    fontSize = 13.sp,
                                    color = Color.DarkGray,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                if (pets.size > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            val newIndex = if (selectedIndex > 0) selectedIndex - 1 else pets.size - 1
                            onPetSelected(newIndex)
                            expanded = false
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Anterior", tint = Color.Gray)
                        }

                        IconButton(onClick = {
                            val newIndex = if (selectedIndex < pets.size - 1) selectedIndex + 1 else 0
                            onPetSelected(newIndex)
                            expanded = false
                        }) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Siguiente", tint = Color.Gray)
                        }
                    }
                }
            } else {
                Text(
                    text = "Detalles y datos sobre la mascota",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}




//Botón para registrar nueva mascota

@Composable
private fun RegisterPetButton(
    onNavigateToPetRegistration: () -> Unit
) {
    Button(
        onClick = onNavigateToPetRegistration,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(horizontal = 40.dp)
    ) {
        Text(
            text = "Registrar mascota",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


//Sección de recordatorios y avisos

@Composable
private fun RemindersAndNoticesSection(
    onNavigateToAppointments: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card de Recordatorios
        Card(
            modifier = Modifier
                .weight(1f)
                .height(150.dp)
                .clickable {
                    Log.d("HomeScreenDebug", "Card de Recordatorios fue presionada.")
                    onNavigateToAppointments()
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = "Recordatorios",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Card de Avisos
        Card(
            modifier = Modifier
                .weight(1f)
                .height(150.dp)
                .clickable {  },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    text = "Avisos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

//funcion solo genera mascotas de muestra
private fun samplePets(): List<Pet> {
    return listOf(
        Pet(
            "1", "Max", "Perro", R.drawable.max,
            listOf(
                "12/01/2024 - Vacuna antirrábica aplicada",
                "20/03/2024 - Desparasitación oral",
                "10/05/2024 - Revisión general, todo en orden"
            )
        ),
        Pet(
            "2", "Luna", "Gato", R.drawable.luna,
            listOf(
                "05/02/2024 - Esterilización",
                "18/04/2024 - Infección leve tratada con antibióticos"
            )
        ),
        Pet(
            "3", "Rocky", "Perro", R.drawable.rocky,
            listOf(
                "01/01/2024 - Control de pulgas",
                "10/06/2024 - Limpieza dental"
            )
        )
    )
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}