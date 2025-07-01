package com.jejhdmdv.proyecto_pdm


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import com.jejhdmdv.proyecto_pdm.navigation.NavGraph
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.loginviewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.common.api.ApiException
import com.jejhdmdv.proyecto_pdm.navigation.NavGraph
import com.jejhdmdv.proyecto_pdm.ui.screens.MainCalendario
import com.jejhdmdv.proyecto_pdm.ui.theme.ProyectoPDMTheme
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.calendarioviewmodel.ReminderViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoPDMTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                val loginViewModel: LoginViewModel = viewModel()

                val googleSignInClient = remember {
                    GoogleSignIn.getClient(
                        context,
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build()
                    )
                }

                val googleSignInLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)

                    } catch (e: ApiException) {

                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 3. Pasa la instancia que acabas de crear
                    NavGraph(
                        navController = navController,
                        loginViewModel = loginViewModel,
                        onGoogleSignInClick = {
                            val signInIntent = googleSignInClient.signInIntent
                            googleSignInLauncher.launch(signInIntent)
                        }
                    )
                }
            }
        }
    }
}

