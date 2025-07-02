package com.jejhdmdv.proyecto_pdm.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.R
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetloginviewmodel.VetLoginViewModel

@Composable
fun VetLoginScreen(
    viewModel: VetLoginViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToVetRegister: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.fillMaxSize().background(Color.White))

        WaveBackground()

        Image(
            painter = painterResource(id = R.drawable.dog_icon),
            contentDescription = "Dog Icon",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp)
                .size(80.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(140.dp))

            Text("PET", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("CARE+ VET", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            Spacer(modifier = Modifier.height(50.dp))

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
                    unfocusedLabelColor = Color.Black
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
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null, tint = Color.Black)
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
                    unfocusedLabelColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        viewModel.login(email, password)
                    } else {
                        Toast.makeText(context, "Campos vacíos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .width(260.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Iniciar Sesión", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "¿Aún no tienes cuenta? Regístrate",
                color = Color.Blue,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigateToVetRegister() }
            )

            Text(
                text = "¿Volver al inicio?",
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { onNavigateBack() }
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        PawPrintsDecoration()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 32.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF73A5A7), RoundedCornerShape(25.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.headphone_icon),
                    contentDescription = "Audífonos",
                    modifier = Modifier.size(30.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color(0xFF73A5A7), RoundedCornerShape(25.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.comment_icon),
                    contentDescription = "Mensajes",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}
