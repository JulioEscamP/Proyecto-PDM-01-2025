package com.jejhdmdv.proyecto_pdm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jejhdmdv.proyecto_pdm.R
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.loginviewmodel.LoginViewModel
import com.jejhdmdv.proyecto_pdm.utils.Resource
import androidx.compose.ui.draw.rotate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onGoogleSignInClick: () -> Unit,
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginResult by viewModel.loginResult.collectAsStateWithLifecycle()

    LaunchedEffect(loginResult) {
        when (loginResult) {
            is Resource.Success -> {
                Toast.makeText(context, "Login exitoso!", Toast.LENGTH_SHORT).show()
            }
            is Resource.Error -> {
                Toast.makeText(context, "Error: ${loginResult.message}", Toast.LENGTH_LONG).show()
            }
            else -> {  }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo principal
        Spacer(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)))

        // Ondas de fondo
        WaveBackground()

        // Icono del perro en la parte superior (sobre la onda rosa)
        Image(
            painter = painterResource(id = R.drawable.dog_icon),
            contentDescription = "Dog Icon",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp)
                .size(80.dp)
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(140.dp))

            // Título
            Text(
                text = "PET",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "CARE+",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Campos de entrada
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Usuario") },
                placeholder = { Text("Ingrese su usuario") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                    focusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    focusedPlaceholderColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                placeholder = { Text("Ingrese su contraseña") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description, tint = Color.Black)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.9f),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                    focusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    focusedPlaceholderColor = Color.Black,
                    unfocusedPlaceholderColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de iniciar sesión
            Button(
                onClick = { viewModel.performLogin(email, password) },
                enabled = loginResult !is Resource.Loading,
                modifier = Modifier
                    .width(260.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (loginResult is Resource.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Iniciar Sesión", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Google
            Button(
                onClick = onGoogleSignInClick, //La funciona la pondre en NavHost
                enabled = loginResult !is Resource.Loading,
                modifier = Modifier
                    .width(260.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google_icon),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Continuar con Google", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Texto de registro
            Text(
                text = "¿Aún no tienes cuenta? Regístrate",
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigateToRegister() }
            )

            Text(
                text = "¿Olvidó su contraseña?",
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // camino de las huellitas
        PawPrintsDecoration()

        // Iconos inferiores
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 32.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // parte inferior izquierda
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF73A5A7), RoundedCornerShape(25.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.headphone_icon),
                    contentDescription = "Audifonos Icon",
                    modifier = Modifier.size(30.dp)
                )
            }

            // Icono de mensajes
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF73A5A7), RoundedCornerShape(25.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.comment_icon),
                    contentDescription = "Mensajes Icon",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Composable
fun WaveBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Colores para el gradiente y las ondas
        val topWaveColor = Color(0xFFCEB6B6)
        val bottomWaveColor = Color(0xFF73A5A7)

        // Onda superior (rosado)
        val wavePathTop = Path().apply {
            moveTo(0f, height * 0.15f)
            quadraticBezierTo(width * 0.25f, height * 0.05f, width * 0.5f, height * 0.15f)
            quadraticBezierTo(width * 0.75f, height * 0.25f, width, height * 0.15f)
            lineTo(width, 0f)
            lineTo(0f, 0f)
            close()
        }
        drawPath(wavePathTop, color = topWaveColor)

        // Onda inferior (azul)
        val wavePathBottom = Path().apply {
            moveTo(0f, height * 0.85f)
            quadraticBezierTo(width * 0.25f, height * 0.95f, width * 0.5f, height * 0.85f)
            quadraticBezierTo(width * 0.75f, height * 0.75f, width, height * 0.85f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        drawPath(wavePathBottom, color = bottomWaveColor)
    }
}

@Composable
fun PawPrintsDecoration() {
    Box(modifier = Modifier.fillMaxSize()) {
        // se dedfine una lista de datos para cada huella: (offset X, offset Y, rotación)
        val pawData = listOf(
            Triple(-7.dp, 200.dp, -15f),
            Triple(-7.dp, 280.dp, 15f),
            Triple(-7.dp, 360.dp, -15f),
            Triple(-7.dp, 440.dp, 15f),
            Triple(-7.dp, 520.dp, -15f),
            Triple(-7.dp, 600.dp, 15f)
        )

        pawData.forEach { (offsetX, offsetY, rotation) ->
            Image(
                painter = painterResource(id = R.drawable.paw_icon),
                contentDescription = "Paw Print",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = offsetX, y = offsetY)
                    .size(30.dp)
                    .rotate(rotation)
            )
        }
    }
}
