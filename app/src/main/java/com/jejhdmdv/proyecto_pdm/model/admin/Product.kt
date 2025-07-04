package com.jejhdmdv.proyecto_pdm.model.admin

/**
 * Modelo de datos para productos en la tienda
 * Utilizado tanto por el administrador como por los usuarios finales
 */
data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val isActive: Boolean = true,
    val createdAt: String = "",
    val updatedAt: String = ""
)

//modelo para crear o actulizar producto
data class ProductRequest(
    val name: String,
    val price: Double,
    val category: String,
    val description: String,
    val imageUrl: String,
    val isActive: Boolean = true
)

//respuesta del servidor
data class ProductResponse(
    val success: Boolean,
    val message: String,
    val product: Product? = null,
    val products: List<Product>? = null
)

