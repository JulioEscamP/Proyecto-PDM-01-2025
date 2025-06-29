package com.jejhdmdv.proyecto_pdm.navigation

/**
 * Clase sellada que define todas las rutas de navegación de la aplicación
 * Cada pantalla tiene su propia ruta única para la navegación
 */
sealed class Screen(val route: String) {
    // Pantallas de autenticación
    object Login : Screen("login")
    object Register : Screen("register")

    // Pantalla principal
    object Home : Screen("home")

    // Pantallas principales de la aplicación
    object PetRegistration : Screen("pet_registration")
    object PetDetailRegistration : Screen("pet_detail_registration/{petType}") {
        fun createRoute(petType: String) = "pet_detail_registration/$petType"
    }
    object Emergency : Screen("emergency")
    object Calendar : Screen("calendar")
    object Appointments : Screen("appointments")
    object Store : Screen("store")
    object Settings : Screen("settings")

    // pantalals para las tiendas
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    object Cart : Screen("cart")
    object Payment : Screen("payment/{totalAmount}") {
        fun createRoute(totalAmount: Double) = "payment/$totalAmount"
    }

    // Pantallas de ajustes
    object FAQ : Screen("faq")
    object TermsAndConditions : Screen("terms_and_conditions")
    object ReportProblem : Screen("report_problem")
}


