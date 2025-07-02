package com.jejhdmdv.proyecto_pdm.model.login

data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    val passwordConfirmation: String
)

data class RegisterResponse(
<<<<<<< Updated upstream
    val userId: String,
    val accessToken: String, // Token del RetrofitClient
    val username: String?
=======
    val message: String,
    val userId: String?
)

data class VetRegisterRequest(
    val nombre: String,
    val email: String,
    val telefono: String,
    val direccion: String,
    val password: String,
    val passwordConfirmation: String,

>>>>>>> Stashed changes
)

data class GoogleSignInRequest(
    val idToken: String
)