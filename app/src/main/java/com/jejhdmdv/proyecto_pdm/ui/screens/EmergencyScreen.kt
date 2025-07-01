package com.jejhdmdv.proyecto_pdm.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.R

data class Veterinary(
    val name: String,
    val address: String,
    val phone: String,
    val imageRes: Int
)

val vetList = listOf(
    Veterinary(
        "Hospital Veterinario Canino Real",
        "Boulevard Universitario #2165, Col. San José, San Salvador",
        "22258089",
        R.drawable.veterinaria1
    ),
    Veterinary(
        "Veterinaria PET VET",
        "Residencial San Francisco Sur, Senda San Francisco 3B, San Salvador",
        "25168858",
        R.drawable.veterinaria2
    ),
    Veterinary(
        "Veterinaria La Mascota",
        "Calle La Mascota #230, San Salvador",
        "22984230",
        R.drawable.veterinaria3
    ),
    Veterinary(
        "Veterinaria Los Héroes",
        "Colonia Los Héroes, San Salvador",
        "22220000",
        R.drawable.veterinaria4
    ),
    Veterinary(
        "Hospital Veterinario Santa Fe",
        "Calle Los Granados #172, Colonia Las Mercedes, San Salvador",
        "22240606",
        R.drawable.veterinaria5
    ),
    Veterinary(
        "TuVet",
        "Lote #22 del Reparto Miralvalle, Cantón San Antonio Abad, San Salvador",
        "23970000",
        R.drawable.veterinaria6
    ),
    Veterinary(
        "Chivo Pets",
        "Carretera Panamericana, frente a La Gran Vía, Antiguo Cuscatlán, La Libertad",
        "195",
        R.drawable.veterinaria7
    )
)

@Composable
fun EmergencyScreen(
    onNavigateToAppointments: () -> Unit,
    onNavigateToStore: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onCallEmergency: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFECE9E6), Color.White)
                )
            )
            .padding(16.dp)
    ) {
        Column {
            // Encabezado "EMERGENCIA" con huellita
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "EMERGENCIA",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE91E63)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = "Icono de Emergencia",
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(26.dp)
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(vetList) { vet ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Image(
                                painter = painterResource(id = vet.imageRes),
                                contentDescription = "Imagen de ${vet.name}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Dirección: ${vet.address}",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Teléfono: ${vet.phone}",
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL)
                                    intent.data = Uri.parse("tel:${vet.phone}")
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE91E63),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Llamar")
                            }
                        }
                    }
                }
            }
        }
    }
}
