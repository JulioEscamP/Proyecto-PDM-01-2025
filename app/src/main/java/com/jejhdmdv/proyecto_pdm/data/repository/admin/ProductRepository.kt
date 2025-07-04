package com.jejhdmdv.proyecto_pdm.data.repository.admin

import com.jejhdmdv.proyecto_pdm.data.remote.admin.ProductApi
import com.jejhdmdv.proyecto_pdm.model.admin.Product
import com.jejhdmdv.proyecto_pdm.model.admin.ProductRequest
import com.jejhdmdv.proyecto_pdm.model.admin.ProductResponse
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Repositorio para la gestión de productos
 * Maneja la comunicación entre el ViewModel y la API
 */
class ProductRepository(private val productApi: ProductApi) {
    
    //obtener todo los productos
    fun getAllProducts(): Flow<Resource<List<Product>>> = flow {
        try {
            emit(Resource.Loading())
            val response = productApi.getAllProducts()
            if (response.isSuccessful) {
                response.body()?.let { productResponse ->
                    if (productResponse.success) {
                        emit(Resource.Success(productResponse.products ?: emptyList()))
                    } else {
                        emit(Resource.Error(productResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
   //productos activos para el usuario final
    fun getActiveProducts(): Flow<Resource<List<Product>>> = flow {
        try {
            emit(Resource.Loading())
            val response = productApi.getActiveProducts()
            if (response.isSuccessful) {
                response.body()?.let { productResponse ->
                    if (productResponse.success) {
                        emit(Resource.Success(productResponse.products ?: emptyList()))
                    } else {
                        emit(Resource.Error(productResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
    //producto por ID
    fun getProductById(productId: String): Flow<Resource<Product>> = flow {
        try {
            emit(Resource.Loading())
            val response = productApi.getProductById(productId)
            if (response.isSuccessful) {
                response.body()?.let { productResponse ->
                    if (productResponse.success && productResponse.product != null) {
                        emit(Resource.Success(productResponse.product))
                    } else {
                        emit(Resource.Error(productResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
    //crear un nuevo producto
    fun createProduct(productRequest: ProductRequest): Flow<Resource<Product>> = flow {
        try {
            emit(Resource.Loading())
            val response = productApi.createProduct(productRequest)
            if (response.isSuccessful) {
                response.body()?.let { productResponse ->
                    if (productResponse.success && productResponse.product != null) {
                        emit(Resource.Success(productResponse.product))
                    } else {
                        emit(Resource.Error(productResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
   //actualizar un producto existente
    fun updateProduct(productId: String, productRequest: ProductRequest): Flow<Resource<Product>> = flow {
        try {
            emit(Resource.Loading())
            val response = productApi.updateProduct(productId, productRequest)
            if (response.isSuccessful) {
                response.body()?.let { productResponse ->
                    if (productResponse.success && productResponse.product != null) {
                        emit(Resource.Success(productResponse.product))
                    } else {
                        emit(Resource.Error(productResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
   //eliminar un producto
    fun deleteProduct(productId: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val response = productApi.deleteProduct(productId)
            if (response.isSuccessful) {
                response.body()?.let { productResponse ->
                    if (productResponse.success) {
                        emit(Resource.Success(productResponse.message))
                    } else {
                        emit(Resource.Error(productResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
}

