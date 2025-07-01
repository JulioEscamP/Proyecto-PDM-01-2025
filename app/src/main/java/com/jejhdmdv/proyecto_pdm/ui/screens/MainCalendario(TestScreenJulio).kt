package com.jejhdmdv.proyecto_pdm.ui.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.calendarioviewmodel.ReminderViewModel

@Composable
fun MainCalendario(viewModel: ReminderViewModel) {
    // ver el estado de la cuenta de Google desde el ViewModel
    val googleAccount by viewModel.googleAccount.collectAsState()
    val context = LocalContext.current

    // Logica para el inicio de sesion de google
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Usuario vuelve de la pantalla de seleccion de cuenta
        Log.d("GoogleSignIn", "Resultado recibido. Código: ${result.resultCode}") // Log para Troubleshoot problema del calendario

        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!

                // Inicio de sesión es exitoso, se actualiza el ViewModel
                Log.d("GoogleSignIn", "¡Inicio de sesión EXITOSO! Email: ${account.email}") // LOG 2
                viewModel.setGoogleAccount(account, context)

            } catch (e: ApiException) {
                // Log 3
                Log.e("GoogleSignIn", "Error al iniciar sesión con Google. Código de estado: ${e.statusCode} Mensaje: ${e.message}", e)
            }
        } else {
            Log.w("GoogleSignIn", "El inicio de sesión fue cancelado o falló. Código: ${result.resultCode}") // LOG 4
        }
    }

    // Comprobar inicio de sesion
    if (googleAccount == null) {
        // --- UI para Iniciar Sesión ---
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = {
                // Prep & Launch - intent de inicio de sesion de google
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail() // Pedir el email del usuario
                    .requestIdToken("75282745771-2saic2io65g1d93fdlaqj6q3d7lqqe3c.apps.googleusercontent.com")
                    .requestScopes(Scope(com.google.api.services.calendar.CalendarScopes.CALENDAR))
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                launcher.launch(googleSignInClient.signInIntent)
            }) {
                Text("Iniciar Sesión con Google")
            }
        }
    } else {
        // --- UI Principal de la App ---
        // mostrar la pantalla de recordatorios (Successful login).
        ReminderScreen(viewModel = viewModel)
    }
}
