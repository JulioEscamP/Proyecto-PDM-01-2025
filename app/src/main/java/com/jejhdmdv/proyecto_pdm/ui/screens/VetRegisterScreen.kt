package com.jejhdmdv.proyecto_pdm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.R
import com.jejhdmdv.proyecto_pdm.model.login.VetRegisterRequest
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetregisterviewmodel.VetRegisterViewModel
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.delay

@Composable
fun VetRegisterScreen(
    viewModel: VetRegisterViewModel,
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    val registerResult by viewModel.registerResult.collectAsState()

    LaunchedEffect(registerResult) {
        when (registerResult) {
            is Resource.Success -> {
                Toast.makeText(context, "Veterinario registrado exitosamente", Toast.LENGTH_LONG).show()
                delay(1000)
                onRegisterSuccess()
            }
            is Resource.Error -> {
                Toast.makeText(context, "Error: ${(registerResult as Resource.Error).message}", Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.fillMaxSize().background(Color.White))

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawWaves(width = size.width, height = size.height)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()), // Scroll activado
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // ✅ Logo ahora se mueve con el scroll
            Image(
                painter = painterResource(id = R.drawable.dog_icon),
                contentDescription = "Icono",
                modifier = Modifier
                    .size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("PET", fontSize = 32.sp, color = Color.Black)
            Text("CARE+", fontSize = 32.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar contraseña") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank() && password == confirmPassword) {
                        viewModel.registerVet(
                            VetRegisterRequest(
                                email = email,
                                password = password,
                                passwordConfirmation = confirmPassword,
                                nombre = nombre,
                                direccion = direccion,
                                telefono = telefono
                            )
                        )
                    } else {
                        Toast.makeText(context, "Verifica los datos ingresados", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Registrarse", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ Botón de regresar (negro, igual diseño que el otro)
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Regresar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        DrawPawPrints()
    }
}

fun DrawScope.drawWaves(width: Float, height: Float) {
    val wavePathTop = Path().apply {
        moveTo(0f, height * 0.15f)
        quadraticBezierTo(width * 0.25f, height * 0.05f, width * 0.5f, height * 0.15f)
        quadraticBezierTo(width * 0.75f, height * 0.25f, width, height * 0.15f)
        lineTo(width, 0f)
        lineTo(0f, 0f)
        close()
    }
    drawPath(wavePathTop, color = Color(0xFFCEB6B6))

    val wavePathBottom = Path().apply {
        moveTo(0f, height * 0.85f)
        quadraticBezierTo(width * 0.25f, height * 0.95f, width * 0.5f, height * 0.85f)
        quadraticBezierTo(width * 0.75f, height * 0.75f, width, height * 0.85f)
        lineTo(width, height)
        lineTo(0f, height)
        close()
    }
    drawPath(wavePathBottom, color = Color(0xFF73A5A7))
}

@Composable
fun DrawPawPrints() {
    val pawData = listOf(
        Triple(-7.dp, 200.dp, -15f),
        Triple(-7.dp, 280.dp, 15f),
        Triple(-7.dp, 360.dp, -15f),
        Triple(-7.dp, 440.dp, 15f),
        Triple(-7.dp, 520.dp, -15f),
        Triple(-7.dp, 600.dp, 15f)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        pawData.forEach { (offsetX, offsetY, rotation) ->
            Image(
                painter = painterResource(id = R.drawable.paw_icon),
                contentDescription = "Huella",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = offsetX, y = offsetY)
                    .size(30.dp)
                    .rotate(rotation)
            )
        }
    }
}
