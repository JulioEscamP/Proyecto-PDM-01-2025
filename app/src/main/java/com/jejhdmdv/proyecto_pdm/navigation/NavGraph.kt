package com.jejhdmdv.proyecto_pdm.navigation

import android.app.Activity
import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.api.services.calendar.CalendarScopes
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
import com.jejhdmdv.proyecto_pdm.ui.screens.VetLoginScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.VetRegisterScreen
import com.jejhdmdv.proyecto_pdm.ui.admin.screens.AdminHomeScreen
import com.jejhdmdv.proyecto_pdm.ui.admin.screens.AdminProfileScreen
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.loginviewmodel.LoginViewModel
import com.jejhdmdv.proyecto_pdm.utils.Resource
import com.jejhdmdv. proyecto_pdm. ui. viewmodels. vetloginviewmodel.VetLoginViewModel
import com.jejhdmdv.proyecto_pdm.ui.viewmodels. vetregisterviewmodel.VetRegisterViewModel
import com.jejhdmdv.proyecto_pdm.ui.screens.VetLoginScreen
import com.jejhdmdv.proyecto_pdm.ui.screens.VetRegisterScreen
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.calendarioviewmodel.ReminderViewModel
import java.time.Instant
import java.time.ZoneId


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
        // --- PANTALLA DE LOGIN ---
        composable(Screen.Login.route) {

            val loginViewModel: LoginViewModel = loginViewModel
            // El contexto para google sign-In
            val context = LocalContext.current
            val TAG = "GoogleSignInDebug"

            // Logica del launcher para recibir el resultado de Google
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                Log.d(TAG, "Resultado recibido. Código de resultado: ${result.resultCode}")
                if (result.resultCode == Activity.RESULT_OK) {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)!!
                        val idToken = account.idToken
                        Log.d(TAG, "¡Inicio de sesión con Google exitoso! Email: ${account.email}")
                        if (idToken != null) {
                            Log.d(TAG, "idToken obtenido, llamando al ViewModel.")
                            loginViewModel.signInWithGoogle(idToken)
                        } else {
                            Log.e(TAG, "Error: idToken de Google es nulo.")
                            Toast.makeText(context, "Error: No se pudo obtener el idToken de Google.", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: ApiException) {
                        Log.e(TAG, "Error de Google Sign-In. Código de estado: ${e.statusCode}", e)
                        Toast.makeText(context, "Error al iniciar sesión con Google: ${e.statusCode}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.w(TAG, "El inicio de sesión fue cancelado o falló. Código: ${result.resultCode}")
                }
            }

            // Logic de navegacion para cuando el login sea exitoso
            val loginResult by loginViewModel.loginResult.collectAsStateWithLifecycle()
            LaunchedEffect(loginResult) {
                if (loginResult is Resource.Success) {
                    // TODO: Verificar si el usuario es administrador basado en la respuesta del backend
                    // Por ahora, usaremos una lógica simple: si el usuario es un administrador, se asume que su nombre de usuario contiene "admin"
                    val isAdmin = loginResult.data?.username?.contains("admin") == true

                    if (isAdmin) {
                        navController.navigate(Screen.AdminHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
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
                        .requestIdToken("75282745771-2saic2io65g1d93fdlaqj6q3d7lqqe3c.apps.googleusercontent.com")
                        .requestScopes(Scope(CalendarScopes.CALENDAR))
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

            val loginResult by vetLoginViewModel.loginResult.collectAsStateWithLifecycle()
            val context = LocalContext.current // Get the current context for Toast messages


            LaunchedEffect(loginResult) {
                when (loginResult) {
                    is Resource.Success -> {
                        Toast.makeText(context, "Inicio de sesión de veterinario exitoso!", Toast.LENGTH_SHORT).show()

                        navController.navigate(Screen.AdminHome.route) {
                            popUpTo(Screen.VetLogin.route) { inclusive = true }
                        }

                        vetLoginViewModel.resetLoginResult()
                    }
                    is Resource.Error -> {
                        val errorMessage = (loginResult as Resource.Error).message ?: "Error desconocido al iniciar sesión."
                        Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
                        vetLoginViewModel.resetLoginResult() // <--- ADD THIS FUNCTION TO VETLOGINVIEWMODEL
                    }
                    is Resource.Loading -> { //

                    }
                    is Resource.Idle -> {

                    }
                }
            }

            // Your VetLoginScreen Composable
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

            val viewModel: ReminderViewModel = viewModel()
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                val account = GoogleSignIn.getLastSignedInAccount(context)
                if (account != null) {
                    viewModel.initialize(account, context)
                } else {
                    Toast.makeText(context, "Error: Usuario no autenticado.", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.appointmentResult) {
                when(val result = uiState.appointmentResult) {
                    is Resource.Success -> {
                        Toast.makeText(context, "Cita confirmada exitosamente!", Toast.LENGTH_SHORT).show()
                        viewModel.resetAppointmentResult()
                        navController.navigate(Screen.Emergency.route) {
                            popUpTo(Screen.Calendar.route) { inclusive = true }
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                        viewModel.resetAppointmentResult()
                    }
                    else -> {} // No hacer nada en Loading
                }
            }

            val unavailableDates = remember(uiState.eventsForMonth) {
                uiState.eventsForMonth.mapNotNull { event ->
                    val eventDate = event.start?.dateTime?.value
                    if (eventDate != null) {
                        Instant.ofEpochMilli(eventDate).atZone(ZoneId.systemDefault()).toLocalDate()
                    } else null
                }.distinct()
            }

            val availableTimeSlots = remember(uiState.selectedDate, uiState.eventsForMonth) {
                val baseSlots = listOf("09:00", "10:00", "11:00", "14:00", "15:00", "16:00")
                val selectedDate = uiState.selectedDate
                if (selectedDate == null) {
                    baseSlots
                } else {
                    val occupiedSlots = uiState.eventsForMonth.mapNotNull { event ->
                        val eventStart = event.start?.dateTime?.value
                        if (eventStart != null) {
                            val eventDateTime = Instant.ofEpochMilli(eventStart).atZone(ZoneId.systemDefault())
                            if (eventDateTime.toLocalDate() == selectedDate) {
                                String.format("%02d:%02d", eventDateTime.hour, eventDateTime.minute)
                            } else null
                        } else null
                    }
                    baseSlots.filter { it !in occupiedSlots }
                }
            }

            // Llama a tu CalendarScreen y conecta todo
            CalendarScreen(
                uiState = uiState,
                onNavigateBack = { navController.popBackStack() },
                onMonthChange = { viewModel.onMonthChange(it) },
                onDateSelected = { viewModel.onDateSelected(it) },
                onTimeSelected = { viewModel.onTimeSelected(it) },
                onConfirmAppointment = { viewModel.confirmAppointment() },
                unavailableDates = unavailableDates,
                availableTimeSlots = availableTimeSlots
            )
        }

        // Pantalla de citas (navega al calendario)
        composable(Screen.Appointments.route) {

            val viewModel: ReminderViewModel = viewModel()
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                val account = GoogleSignIn.getLastSignedInAccount(context)
                if (account != null) {
                    viewModel.initialize(account, context)
                } else {
                    Toast.makeText(context, "Error: Usuario no autenticado.", Toast.LENGTH_LONG).show()
                    navController.popBackStack()
                }
            }

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val appointmentResult by viewModel.appointmentResult.collectAsStateWithLifecycle()

            LaunchedEffect(appointmentResult) {
                when(val result = appointmentResult) {
                    is Resource.Success -> {
                        Toast.makeText(context, "Cita confirmada exitosamente!", Toast.LENGTH_SHORT).show()
                        viewModel.resetAppointmentResult() // Resetea para evitar múltiples Toasts/navegaciones
                        navController.navigate(Screen.Emergency.route) {
                            popUpTo(Screen.Appointments.route) { inclusive = true }
                        }
                    }
                    is Resource.Error -> {
                        Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                        viewModel.resetAppointmentResult()
                    }
                    else -> {}
                }
            }

            val unavailableDates = remember(uiState.eventsForMonth) {
                uiState.eventsForMonth.mapNotNull { event ->
                    event.start?.dateTime?.value?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                }.distinct()
            }

            val availableTimeSlots = remember(uiState.selectedDate, uiState.eventsForMonth) {
                val baseSlots = listOf("09:00", "10:00", "11:00", "14:00", "15:00", "16:00")
                val selectedDate = uiState.selectedDate ?: return@remember emptyList()

                val occupiedSlots = uiState.eventsForMonth.mapNotNull { event ->
                    event.start?.dateTime?.value?.let {
                        val eventDateTime = Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault())
                        if (eventDateTime.toLocalDate() == selectedDate) {
                            String.format("%02d:%02d", eventDateTime.hour, eventDateTime.minute)
                        } else null
                    }
                }
                baseSlots.filter { it !in occupiedSlots }
            }

            CalendarScreen(
                uiState = uiState,
                unavailableDates = unavailableDates,
                availableTimeSlots = availableTimeSlots,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onMonthChange = { viewModel.onMonthChange(it) },
                onDateSelected = { viewModel.onDateSelected(it) },
                onTimeSelected = { viewModel.onTimeSelected(it) },
                onConfirmAppointment = { viewModel.confirmAppointment() }
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



        // Pantalla principal del administrador
        composable(Screen.AdminHome.route) {
            AdminHomeScreen(
                onNavigateToProducts = {
                    navController.navigate(Screen.AdminProductList.route)
                },
                onNavigateToClinics = {
                    navController.navigate(Screen.AdminClinicList.route)
                },
                onNavigateToAppointments = {
                    navController.navigate(Screen.AdminAppointmentList.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.AdminProfile.route)
                },
                onLogout = {
                    navController.navigate(Screen.VetLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de perfil del administrador
        composable(Screen.AdminProfile.route) {
            AdminProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screen.VetLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSaveProfile = { name, address, phone ->
                    // TODO: Implementar guardado de perfil
                    navController.popBackStack()
                }
            )
        }

        // Lista de productos (Admin)
        composable(Screen.AdminProductList.route) {
            // TODO: Implementar con ViewModel
            PlaceholderScreen(
                title = "Lista de Productos",
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Formulario de producto (Admin)
        composable(Screen.AdminProductForm.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: "new"
            // TODO: Implementar con ViewModel
            PlaceholderScreen(
                title = if (productId == "new") "Nuevo Producto" else "Editar Producto",
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Lista de clínicas (Admin)
        composable(Screen.AdminClinicList.route) {
            PlaceholderScreen(
                title = "Lista de Clínicas",
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Formulario de clínica (Admin)
        composable(Screen.AdminClinicForm.route) { backStackEntry ->
            val clinicId = backStackEntry.arguments?.getString("clinicId") ?: "new"
            PlaceholderScreen(
                title = if (clinicId == "new") "Nueva Clínica" else "Editar Clínica",
                onNavigateBack = { navController.popBackStack() }
            )
        }


        composable(Screen.AdminAppointmentList.route) {

            PlaceholderScreen(
                title = "Gestión de Citas",
                onNavigateBack = { navController.popBackStack() }
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
