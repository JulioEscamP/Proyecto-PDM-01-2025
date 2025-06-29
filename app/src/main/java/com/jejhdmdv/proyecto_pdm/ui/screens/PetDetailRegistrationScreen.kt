package com.jejhdmdv.proyecto_pdm.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.R

/**
 * Pantalla de registro detallado de mascota
 * Aparece cuando el usuario selecciona un tipo específico de mascota
 * Permite ingresar: Nombre, Edad, Raza/Clase, Peso, Altura
 */
@Composable
fun PetDetailRegistrationScreen(
    petType: PetType,
    onNavigateBack: () -> Unit = {},
    onSavePet: (PetDetails) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Estados para los campos del formulario
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }

    //  fondo
    val colorLeft = Color(0xFF73A5A7)
    val colorRight = Color(0xFFCEB6B6)

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Fondo con forma de zigzag vertical
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val zigzagWidth = width * 0.5f
            val zigzagAmplitude = 50f
            val numZigzags = 5

            // Dibujar la parte izquierda
            drawRect(color = colorLeft, size = size)

            // Dibujar la parte derecha
            val path = Path().apply {
                moveTo(zigzagWidth, 0f)
                for (i in 0..numZigzags) {
                    val y = i * (height / numZigzags)
                    val x = zigzagWidth + (if (i % 2 == 0) zigzagAmplitude else -zigzagAmplitude)
                    lineTo(x, y)
                }
                lineTo(width, height)
                lineTo(width, 0f)
                close()
            }
            drawPath(path, color = colorRight)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // Título de la sección
            TitleSection(petType = petType)

            Spacer(modifier = Modifier.height(40.dp))

            // Icono del tipo de mascota seleccionado
            PetTypeIcon(petType = petType)

            Spacer(modifier = Modifier.height(40.dp))

            // Formulario de registro
            PetRegistrationForm(
                nombre = nombre,
                edad = edad,
                raza = raza,
                peso = peso,
                altura = altura,
                petType = petType,
                onNombreChange = { nombre = it },
                onEdadChange = { edad = it },
                onRazaChange = { raza = it },
                onPesoChange = { peso = it },
                onAlturaChange = { altura = it }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Botones de acción
            ActionButtons(
                onNavigateBack = onNavigateBack,
                onSavePet = {
                    val petDetails = PetDetails(
                        nombre = nombre,
                        edad = edad.toIntOrNull() ?: 0,
                        raza = raza,
                        peso = peso.toFloatOrNull() ?: 0f,
                        altura = altura.toFloatOrNull() ?: 0f,
                        tipo = petType
                    )
                    onSavePet(petDetails)
                },
                isFormValid = nombre.isNotBlank() && edad.isNotBlank() &&
                        raza.isNotBlank() && peso.isNotBlank() && altura.isNotBlank()
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // Icono de perro en la parte inferior
        Image(
            painter = painterResource(id = R.drawable.dog_icon),
            contentDescription = "Dog Silhouette",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 5.dp)
                .size(50.dp)
        )
    }
}

/**
 * Sección del título dinámico según el tipo de mascota
 */
@Composable
private fun TitleSection(petType: PetType) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mascota a ingresar ",
            fontSize = 18.sp,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}

//icono del tipo de mascota seleccioanda
@Composable
private fun PetTypeIcon(petType: PetType) {
    val iconRes = when (petType) {
        PetType.DOG -> R.drawable.dog_face_icon
        PetType.CAT -> R.drawable.cat_face_icon
        PetType.OTHER -> R.drawable.other_pet_icon
    }

    Image(
        painter = painterResource(id = iconRes),
        contentDescription = "Selected Pet Type",
        modifier = Modifier.size(60.dp)
    )
}

//formulario de registro
@Composable
private fun PetRegistrationForm(
    nombre: String,
    edad: String,
    raza: String,
    peso: String,
    altura: String,
    petType: PetType,
    onNombreChange: (String) -> Unit,
    onEdadChange: (String) -> Unit,
    onRazaChange: (String) -> Unit,
    onPesoChange: (String) -> Unit,
    onAlturaChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Campo Nombre
        CustomTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = "Nombre:",
            placeholder = ""
        )

        // Campo Edad
        CustomTextField(
            value = edad,
            onValueChange = onEdadChange,
            label = "Edad:",
            placeholder = "",
            keyboardType = KeyboardType.Number
        )

        // Campo Raza o Clase de mascota
        val razaLabel = if (petType == PetType.OTHER) "Clase de mascota:" else "Raza:"
        CustomTextField(
            value = raza,
            onValueChange = onRazaChange,
            label = razaLabel,
            placeholder = ""
        )

        // Campo Peso
        CustomTextField(
            value = peso,
            onValueChange = onPesoChange,
            label = "Peso:",
            placeholder = "",
            keyboardType = KeyboardType.Decimal
        )

        // Campo Altura
        CustomTextField(
            value = altura,
            onValueChange = onAlturaChange,
            label = "Altura:",
            placeholder = "",
            keyboardType = KeyboardType.Decimal
        )
    }
}

  //Campo de texto personalizado

@Composable
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true
        )
    }
}

/**
 * Botones de acción: Regresar y Guardar
 */
@Composable
private fun ActionButtons(
    onNavigateBack: () -> Unit,
    onSavePet: () -> Unit,
    isFormValid: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Botón Regresar
        Button(
            onClick = onNavigateBack,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Regresar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Botón Guardar
        Button(
            onClick = onSavePet,
            enabled = isFormValid,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray.copy(alpha = 0.6f),
                disabledContentColor = Color.White
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Guardar",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

//data paara los detalles de la mascota
data class PetDetails(
    val nombre: String,
    val edad: Int,
    val raza: String,
    val peso: Float,
    val altura: Float,
    val tipo: PetType
)


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PetDetailRegistrationScreenPreview() {
    PetDetailRegistrationScreen(petType = PetType.DOG)
}
