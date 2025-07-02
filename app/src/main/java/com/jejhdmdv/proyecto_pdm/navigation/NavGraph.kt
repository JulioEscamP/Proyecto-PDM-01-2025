package com.jejhdmdv.proyecto_pdm.navigation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.jejhdmdv.proyecto_pdm.ui.screens.EmergencyScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.HomeScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.LoginScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.PetRegistrationScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.PetDetailRegistrationScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.PetType
import com.jejhdmdv.proyecto_pdm.ui.screens.RegisterScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.CalendarScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.SettingsScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.FAQScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.TermsAndConditionsScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.ReportProblemScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.StoreScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.ProductDetailScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.CartScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.PaymentScreen
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.loginviewmodel.LoginViewModel
import com.jejhdmdv.proyecto_pdm.utils.Resource
import com.jejhdmdv. proyecto_pdm. ui. viewmodels. vetloginviewmodel.VetLoginViewModel
import com.jejhdmdv.proyecto_pdm.ui.viewmodels. vetregisterviewmodel.VetRegisterViewModel
import com.jejhdmdv.proyecto_pdm.ui.screens.VetLoginScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.VetRegisterScreen

/**
 * Composable que define el grafo de navegación de la aplicación
 * Maneja todas las rutas y transiciones entre pantallas
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    vetLoginViewModel: VetLoginViewModel,
    vetRegisterViewModel: VetRegisterViewModel,
    onGoogleSignInClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Pantalla de inicio de sesión
        composable(Screen.Login.route) {

            val loginViewModel: LoginViewModel = loginViewModel
            // El contexto para google sign-In
            val context = LocalContext.current

            // Logica del launcher para recibir el resultado de Google
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)!!
                        val idToken = account.idToken
                        if (idToken != null) {
                            loginViewModel.signInWithGoogle(idToken)
                        } else {
                            // Manejar el caso de token nulo
                            Toast.makeText(context, "Error: idToken de Google es nulo.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: ApiException) {
                        // Manejar error de la API
                        Toast.makeText(context, "Error de Google Sign-In: ${e.statusCode}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Logic de navegacion para cuando el login sea exitoso
            val loginResult by loginViewModel.loginResult.collectAsStateWithLifecycle()
            LaunchedEffect(loginResult) {
                if (loginResult is Resource.Success) {
                    // Navega a la pantalla principal y limpia la pila de navegación
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            // LoginScreen con toda la logica conectada
            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },

                onGoogleSignInClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken("75282745771-197vm5m67kuec92bj5o22ju2bf2ap4kl.apps.googleusercontent.com")
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)

                    googleSignInClient.signOut().addOnCompleteListener {
                        launcher.launch(googleSignInClient.signInIntent)
                    }
                },

                onNavigateToVetLogin = {
                    navController.navigate(Screen.VetLogin.route)
                }
            )
        }

        // Pantalla de registro
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = loginViewModel, // Actualice y ahora ocupa el viewmodel que tengo para registro
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        // Elimina las pantallas de Login y Registro del historial.
                        // usuario no podrá volver a ellas con el botón "atrás".
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Pantalla principal (Home)
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToEmergency = {
                    navController.navigate(Screen.Emergency.route)
                },
                onNavigateToAppointments = {
                    navController.navigate(Screen.Appointments.route)
                },
                onNavigateToStore = {
                    navController.navigate(Screen.Store.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToPetRegistration = {
                    navController.navigate(Screen.PetRegistration.route)
                }
            )
        }
 // Pantalla de login del vet
        composable(Screen.VetLogin.route) {
            VetLoginScreen(
                viewModel = vetLoginViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVetRegister = {
                    navController.navigate(Screen.VetRegister.route)
                }
            )

        }
        //
        composable(Screen.VetRegister.route) {
            VetRegisterScreen(
                viewModel = vetRegisterViewModel,
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.VetLogin.route) {
                        popUpTo(Screen.VetRegister.route) { inclusive = true }
                    }
                }
            )


    }

        // Pantalla de registro de mascota
        composable(Screen.PetRegistration.route) {
            PetRegistrationScreen(
                onNavigateToEmergency = {
                    navController.navigate(Screen.Emergency.route) {
                        // Limpiar el stack para que Emergency =(
                        popUpTo(Screen.PetRegistration.route) { inclusive = true }
                    }
                },
                onPetTypeSelected = { petType ->
                    val petTypeString = when (petType) {
                        PetType.DOG -> "DOG"
                        PetType.CAT -> "CAT"
                        PetType.OTHER -> "OTHER"
                    }
                    navController.navigate(Screen.PetDetailRegistration.createRoute(petTypeString))
                }
            )
        }

        // Pantalla de registro detallado de mascota
        composable(Screen.PetDetailRegistration.route) { backStackEntry ->
            val petTypeString = backStackEntry.arguments?.getString("petType") ?: "DOG"
            val petType = when (petTypeString) {
                "DOG" -> PetType.DOG
                "CAT" -> PetType.CAT
                "OTHER" -> PetType.OTHER
                else -> PetType.DOG
            }

            PetDetailRegistrationScreen(
                petType = petType,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSavePet = { petDetails ->
                    // aqui se  Guardan los datos de la mascota
                    navController.navigate(Screen.Emergency.route) {
                        popUpTo(Screen.PetRegistration.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de emergencia (pantalla principal con bottom navigation)
        composable(Screen.Emergency.route) {
            EmergencyScreen(
                onNavigateToAppointments = {
                    navController.navigate(Screen.Appointments.route)
                },
                onNavigateToStore = {
                    navController.navigate(Screen.Store.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onCallEmergency = {
                    // TODO: Implementar funcionalidad de llamada de emergencia
                }
            )
        }

        // Pantalla de calendario
        composable(Screen.Calendar.route) {
            CalendarScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onConfirmAppointment = { date, time ->
                    // TODO: Guardar la cita
                    navController.navigate(Screen.Emergency.route) {
                        popUpTo(Screen.Calendar.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de citas (navega al calendario)
        composable(Screen.Appointments.route) {
            CalendarScreen(
                onNavigateBack = {
                    navController.navigate(Screen.Emergency.route) {
                        popUpTo(Screen.Emergency.route) { inclusive = true }
                    }
                },
                onConfirmAppointment = { date, time ->
                    // TODO: Guardar la cita
                    navController.navigate(Screen.Emergency.route) {
                        popUpTo(Screen.Appointments.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de tienda
        composable(Screen.Store.route) {
            StoreScreen(
                onNavigateToAppointments = {
                    navController.navigate(Screen.Appointments.route)
                },
                onNavigateToEmergency = {
                    navController.navigate(Screen.Emergency.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToProductDetail = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                }
            )
        }

        // Pantalla de detalle del producto
        composable(Screen.ProductDetail.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAddToCart = { productId ->
                    //  Agregar producto al carrito
                    navController.navigate(Screen.Cart.route)
                }
            )
        }

        // Pantalla del carrito
        composable(Screen.Cart.route) {
            CartScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onCheckout = { totalAmount ->
                    navController.navigate(Screen.Payment.createRoute(totalAmount))
                }
            )
        }

        // Pantalla de pago
        composable(Screen.Payment.route) { backStackEntry ->
            val totalAmountString = backStackEntry.arguments?.getString("totalAmount") ?: "0.0"
            val totalAmount = totalAmountString.toDoubleOrNull() ?: 0.0
            PaymentScreen(
                totalAmount = totalAmount,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onProcessPayment = { paymentData ->
                    // TODO: Implementar procesamiento de pago
                    // navegar de vuelta a la tienda con confirmación
                    navController.navigate(Screen.Store.route) {
                        popUpTo(Screen.Store.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de ajustes
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateToAppointments = {
                    navController.navigate(Screen.Appointments.route)
                },
                onNavigateToEmergency = {
                    navController.navigate(Screen.Emergency.route)
                },
                onNavigateToStore = {
                    navController.navigate(Screen.Store.route)
                },
                onNavigateToFAQ = {
                    navController.navigate(Screen.FAQ.route)
                },
                onNavigateToTerms = {
                    navController.navigate(Screen.TermsAndConditions.route)
                },
                onNavigateToReportProblem = {
                    navController.navigate(Screen.ReportProblem.route)
                },
                onLogout = {
                    // TODO: Implementar lógica de cerrar sesión
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de preguntas frecuentes
        composable(Screen.FAQ.route) {
            FAQScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de términos y condiciones
        composable(Screen.TermsAndConditions.route) {
            TermsAndConditionsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de reportar problema
        composable(Screen.ReportProblem.route) {
            ReportProblemScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onReportProblem = { selectedProblems, otherProblem ->
                    // TODO: Implementar lógica para enviar el reporte
                    navController.popBackStack()
                }
            )
        }
    }
}

/*  pantallas que aun no estan implementadas */
@Composable
private fun PlaceholderScreen(
    title: String,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pantalla en desarrollo",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onNavigateBack) {
                Text("Regresar")
            }
        }
    }
}

