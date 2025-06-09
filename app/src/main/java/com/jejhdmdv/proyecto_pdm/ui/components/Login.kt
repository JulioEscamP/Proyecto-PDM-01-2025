package com.jejhdmdv.proyecto_pdm.ui.components

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.jejhdmdv.proyecto_pdm.data.remote.RetrofitClient
import com.jejhdmdv.proyecto_pdm.data.repository.AuthRepository
import com.jejhdmdv.proyecto_pdm.ui.theme.ProyectoPDMTheme
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.LoginViewModel
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.LoginViewModelFactory
import com.jejhdmdv.proyecto_pdm.utils.Resource

class LoginActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent> // Para el nuevo enfoque de ActivityResultLauncher

    private lateinit var loginViewModel: LoginViewModel // <-- Renombrada para claridad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = AuthRepository(RetrofitClient.authApiService)
        val viewModelFactory = LoginViewModelFactory(repository)
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)


        // Configurar Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("WEB_CLIENT_ID") // TODO: Reemplaza con tu ID de cliente de Google - Ya tengo el ID en la consola de Google
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Inicializar ActivityResultLauncher para Google
        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    val idToken = account.idToken

                    if (idToken != null) {
                        loginViewModel.signInWithGoogle(idToken)
                    } else {
                        Toast.makeText(this, "No se pudo obtener el ID Token de Google.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: ApiException) {
                    Toast.makeText(this, "Google Sign-in falló: ${e.statusCode}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Inicio de sesión con Google cancelado.", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            ProyectoPDMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(
                        onGoogleSignInClick = {
                            val signInIntent = googleSignInClient.signInIntent
                            googleSignInLauncher.launch(signInIntent)
                        },
                        viewModel = loginViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Anotación para TextField, Button, etc.
@Composable
fun LoginScreen(
    onGoogleSignInClick: () -> Unit,
    viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(AuthRepository(RetrofitClient.authApiService))
    )
) {
    val context = LocalContext.current // Para mostrar Toasts

    //campos de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //
    val loginResult by viewModel.loginResult.collectAsStateWithLifecycle() // Usa collectAsStateWithLifecycle para LiveData/StateFlow

    // Efecto secundario para reaccionar a los cambios en loginResult
    LaunchedEffect(loginResult) {
        when (loginResult) {
            is Resource.Success -> {
                Toast.makeText(context, "Login exitoso!", Toast.LENGTH_SHORT).show()
                // Aquí podrías navegar a la siguiente pantalla
                // val accessToken = (loginResult as Resource.Success).data?.accessToken
                // Guardar token y navegar
            }
            is Resource.Error -> {
                Toast.makeText(context, "Error: ${loginResult.message}", Toast.LENGTH_LONG).show()
            }
            else -> { /* No hacer nada para Loading o Idle/Initial */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¡Bienvenido de nuevo!",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.foundation.text.KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.foundation.text.KeyboardType.Password),
            visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.performLogin(email, password) },
            enabled = loginResult !is Resource.Loading, // Deshabilita el botón mientras carga
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onGoogleSignInClick,
            enabled = loginResult !is Resource.Loading,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer) // Ejemplo de color
        ) {
            Text("Iniciar Sesión con Google")
        }

        if (loginResult is Resource.Loading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator() // Indicador de progreso
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    ProyectoPDMTheme {
        LoginScreen(onGoogleSignInClick = {})
    }
}