package com.jejhdmdv.proyecto_pdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.jejhdmdv.proyecto_pdm.data.remote.RetrofitClient
import com.jejhdmdv.proyecto_pdm.data.repository.AuthRepository
import com.jejhdmdv.proyecto_pdm.navigation.NavGraph
import com.jejhdmdv.proyecto_pdm.ui.theme.ProyectoPDMTheme
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.loginviewmodel.LoginViewModel
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.loginviewmodel.LoginViewModelFactory
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetloginviewmodel.VetLoginViewModel
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetloginviewmodel.VetLoginViewModelFactory
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetregisterviewmodel.VetRegisterViewModel
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetregisterviewmodel.VetRegisterViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProyectoPDMTheme {
                val context = LocalContext.current
                val navController = rememberNavController()

                // Retrofit y repositorio
                val authApiService = remember { RetrofitClient.authApiService }
                val authRepository = remember { AuthRepository(authApiService) }

                // Factories para los ViewModels
                val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(authRepository))
                val vetLoginViewModel: VetLoginViewModel = viewModel(factory = VetLoginViewModelFactory(authRepository))
                val vetRegisterViewModel: VetRegisterViewModel = viewModel(factory = VetRegisterViewModelFactory(authRepository))

                // Google Sign-In
                val googleSignInClient = remember {
                    GoogleSignIn.getClient(
                        context,
                        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .requestIdToken("75282745771-197vm5m67kuec92bj5o22ju2bf2ap4kl.apps.googleusercontent.com")
                            .build()
                    )
                }

                val googleSignInLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)
                        val idToken = account.idToken
                        if (idToken != null) {
                            loginViewModel.signInWithGoogle(idToken)
                        }
                    } catch (e: ApiException) {
                        // Manejo de error si falla el login con Google
                        e.printStackTrace()
                    }
                }

                // Pantalla principal
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(
                        navController = navController,
                        loginViewModel = loginViewModel,
                        vetLoginViewModel = vetLoginViewModel,
                        vetRegisterViewModel = vetRegisterViewModel,
                        onGoogleSignInClick = {
                            googleSignInClient.signOut().addOnCompleteListener {
                                googleSignInLauncher.launch(googleSignInClient.signInIntent)
                            }
                        }
                    )
                }
            }
        }
    }
}