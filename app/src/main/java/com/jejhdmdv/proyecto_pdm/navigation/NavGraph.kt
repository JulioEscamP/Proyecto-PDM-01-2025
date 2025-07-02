package com.jejhdmdv.proyecto_pdm.navigation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.jejhdmdv.proyecto_pdm.ui.screens.*
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.loginviewmodel.LoginViewModel
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetloginviewmodel.VetLoginViewModel
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetregisterviewmodel.VetRegisterViewModel
import com.jejhdmdv.proyecto_pdm.utils.Resource

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

        // Login de usuario
        composable(Screen.Login.route) {
            val context = LocalContext.current
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
                            Toast.makeText(context, "Error: idToken de Google es nulo.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: ApiException) {
                        Toast.makeText(context, "Error de Google Sign-In: ${e.statusCode}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            val loginResult by loginViewModel.loginResult.collectAsStateWithLifecycle()
            LaunchedEffect(loginResult) {
                if (loginResult is Resource.Success) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            LoginScreen(
                viewModel = loginViewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
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
                onNavigateToVetLogin = { navController.navigate(Screen.VetLogin.route) }
            )
        }

        // Registro de usuario
        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = loginViewModel,
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Home
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToEmergency = { navController.navigate(Screen.Emergency.route) },
                onNavigateToAppointments = { navController.navigate(Screen.Appointments.route) },
                onNavigateToStore = { navController.navigate(Screen.Store.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToPetRegistration = { navController.navigate(Screen.PetRegistration.route) }
            )
        }

        // Login veterinario
        composable(Screen.VetLogin.route) {
            VetLoginScreen(
                viewModel = vetLoginViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVetRegister = { navController.navigate(Screen.VetRegister.route) }
            )
        }

        // Registro veterinario
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

        // Registro de mascotas
        composable(Screen.PetRegistration.route) {
            PetRegistrationScreen(
                onNavigateToEmergency = {
                    navController.navigate(Screen.Emergency.route) {
                        popUpTo(Screen.PetRegistration.route) { inclusive = true }
                    }
                },
                onPetTypeSelected = { petType ->
                    val petTypeString = petType.name
                    navController.navigate(Screen.PetDetailRegistration.createRoute(petTypeString))
                }
            )
        }

        // Registro detallado
        composable(Screen.PetDetailRegistration.route) { backStackEntry ->
            val petTypeString = backStackEntry.arguments?.getString("petType") ?: "DOG"
            val petType = PetType.valueOf(petTypeString)

            PetDetailRegistrationScreen(
                petType = petType,
                onNavigateBack = { navController.popBackStack() },
                onSavePet = {
                    navController.navigate(Screen.Emergency.route) {
                        popUpTo(Screen.PetRegistration.route) { inclusive = true }
                    }
                }
            )
        }

        // Emergencia
        composable(Screen.Emergency.route) {
            EmergencyScreen(
                onNavigateToAppointments = { navController.navigate(Screen.Appointments.route) },
                onNavigateToStore = { navController.navigate(Screen.Store.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onCallEmergency = { /* Implementar llamada */ }
            )
        }

        // Citas
        composable(Screen.Appointments.route) {
            CalendarScreen(
                onNavigateBack = {
                    navController.navigate(Screen.Emergency.route) {
                        popUpTo(Screen.Emergency.route) { inclusive = true }
                    }
                },
                onConfirmAppointment = { _, _ ->
                    navController.navigate(Screen.Emergency.route) {
                        popUpTo(Screen.Appointments.route) { inclusive = true }
                    }
                }
            )
        }

        // Tienda
        composable(Screen.Store.route) {
            StoreScreen(
                onNavigateToAppointments = { navController.navigate(Screen.Appointments.route) },
                onNavigateToEmergency = { navController.navigate(Screen.Emergency.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToProductDetail = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onNavigateToCart = { navController.navigate(Screen.Cart.route) }
            )
        }

        // Detalle producto
        composable(Screen.ProductDetail.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                onNavigateBack = { navController.popBackStack() },
                onAddToCart = { navController.navigate(Screen.Cart.route) }
            )
        }

        // Carrito
        composable(Screen.Cart.route) {
            CartScreen(
                onNavigateBack = { navController.popBackStack() },
                onCheckout = { totalAmount ->
                    navController.navigate(Screen.Payment.createRoute(totalAmount))
                }
            )
        }

        // Pago
        composable(Screen.Payment.route) { backStackEntry ->
            val totalAmount = backStackEntry.arguments?.getString("totalAmount")?.toDoubleOrNull() ?: 0.0
            PaymentScreen(
                totalAmount = totalAmount,
                onNavigateBack = { navController.popBackStack() },
                onProcessPayment = {
                    navController.navigate(Screen.Store.route) {
                        popUpTo(Screen.Store.route) { inclusive = true }
                    }
                }
            )
        }

        // Ajustes
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateToAppointments = { navController.navigate(Screen.Appointments.route) },
                onNavigateToEmergency = { navController.navigate(Screen.Emergency.route) },
                onNavigateToStore = { navController.navigate(Screen.Store.route) },
                onNavigateToFAQ = { navController.navigate(Screen.FAQ.route) },
                onNavigateToTerms = { navController.navigate(Screen.TermsAndConditions.route) },
                onNavigateToReportProblem = { navController.navigate(Screen.ReportProblem.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // FAQ
        composable(Screen.FAQ.route) {
            FAQScreen(onNavigateBack = { navController.popBackStack() })
        }

        // Términos
        composable(Screen.TermsAndConditions.route) {
            TermsAndConditionsScreen(onNavigateBack = { navController.popBackStack() })
        }

        // Reporte problema
        composable(Screen.ReportProblem.route) {
            ReportProblemScreen(
                onNavigateBack = { navController.popBackStack() },
                onReportProblem = { _, _ -> navController.popBackStack() }
            )
        }
    }
}
