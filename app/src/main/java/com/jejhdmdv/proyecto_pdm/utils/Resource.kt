package com.jejhdmdv.proyecto_pdm.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    class Loading<T>(data: T? = null) : Resource<T>(data)

    class Success<T>(data: T) : Resource<T>(data)

    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    // Representa el estado inicial, antes de cualquier operación.
    class Idle<T> : Resource<T>()
}