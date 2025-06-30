package com.jejhdmdv.proyecto_pdm.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.R


@Composable
fun PetRegistrationScreen(
    onNavigateToEmergency: () -> Unit = {},
    onPetTypeSelected: (PetType) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Estado para controlar qué tipo de mascota está seleccionada
    var selectedPetType by remember { mutableStateOf<PetType?>(null) }

    // Contenedor principal
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF73A5A7),
                        Color(0xFFCEB6B6)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Sección del título principal
            TitleSection()

            Spacer(modifier = Modifier.height(60.dp))

            // Sección de registro de mascota
            PetRegistrationSection(
                selectedPetType = selectedPetType,
                onPetTypeSelected = { selectedPetType = it }
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Botón de continuar
            ContinueButton(
                isEnabled = selectedPetType != null,
                onClick = {
                    selectedPetType?.let { petType ->
                        onPetTypeSelected(petType)
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // Icono de perro en la parte inferior
        Image(
            painter = painterResource(id = R.drawable.dog_icon),
            contentDescription = "Dog Silhouette",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp)
                .size(80.dp)
        )
    }
}

//titulo de la pantalla
@Composable
private fun TitleSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selecciona tu",
            fontSize = 28.sp,
            color = Color.White,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )

        Text(
            text = "MASCOTA",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}


  //Sección de registro de tipo de mascota

@Composable
private fun PetRegistrationSection(
    selectedPetType: PetType?,
    onPetTypeSelected: (PetType) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Opción: Perro
        PetOptionCard(
            petType = PetType.DOG,
            iconRes = R.drawable.dog_face_icon,
            label = "Perro",
            isSelected = selectedPetType == PetType.DOG,
            onClick = { onPetTypeSelected(PetType.DOG) }
        )

        // Opción: Gato
        PetOptionCard(
            petType = PetType.CAT,
            iconRes = R.drawable.cat_face_icon,
            label = "Gato",
            isSelected = selectedPetType == PetType.CAT,
            onClick = { onPetTypeSelected(PetType.CAT) }
        )

        // Opción: Otro
        PetOptionCard(
            petType = PetType.OTHER,
            iconRes = R.drawable.other_pet_icon,
            label = "Otro",
            isSelected = selectedPetType == PetType.OTHER,
            onClick = { onPetTypeSelected(PetType.OTHER) }
        )
    }
}

//card individual para cada opcion de mascota
@Composable
private fun PetOptionCard(
    petType: PetType,
    iconRes: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick() }
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 3.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    Modifier
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Icono de la mascota
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.width(24.dp))

            // Etiqueta de texto
            Text(
                text = label,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

//boton continuar
@Composable
private fun ContinueButton(
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray.copy(alpha = 0.6f),
            disabledContentColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Continuar",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

//tipos de mascota disponibles
enum class PetType {
    DOG,
    CAT,
    OTHER
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PetRegistrationScreenPreview() {
    PetRegistrationScreen()
}

