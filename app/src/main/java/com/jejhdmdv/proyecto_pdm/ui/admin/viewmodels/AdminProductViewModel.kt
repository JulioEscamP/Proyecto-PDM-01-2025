package com.jejhdmdv.proyecto_pdm.ui.admin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jejhdmdv.proyecto_pdm.data.repository.admin.ProductRepository
import com.jejhdmdv.proyecto_pdm.model.admin.Product
import com.jejhdmdv.proyecto_pdm.model.admin.ProductRequest
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class AdminProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    
    // Estado de la lista de productos
    private val _productsState = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val productsState: StateFlow<Resource<List<Product>>> = _productsState.asStateFlow()
    
    // Estado del producto individual (para edición)
    private val _productState = MutableStateFlow<Resource<Product>?>(null)
    val productState: StateFlow<Resource<Product>?> = _productState.asStateFlow()
    
    // Estado de operaciones (crear, actualizar, eliminar)
    private val _operationState = MutableStateFlow<Resource<String>?>(null)
    val operationState: StateFlow<Resource<String>?> = _operationState.asStateFlow()
    
    init {
        loadProducts()
    }
    

    fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts().collect { result ->
                _productsState.value = result
            }
        }
    }
    

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            productRepository.getProductById(productId).collect { result ->
                _productState.value = result
            }
        }
    }
    

    fun createProduct(productRequest: ProductRequest) {
        viewModelScope.launch {
            productRepository.createProduct(productRequest).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _operationState.value = Resource.Success("Producto creado exitosamente")
                        loadProducts()
                    }
                    is Resource.Error -> {
                        _operationState.value = Resource.Error(result.message ?: "Error al crear producto")
                    }
                    is Resource.Loading -> {
                        _operationState.value = Resource.Loading()
                    }
                    else -> {
                        // Handle Idle or any other unhandled state
                        _operationState.value = Resource.Idle()
                    }
                }
            }
        }
    }
    

    fun updateProduct(productId: String, productRequest: ProductRequest) {
        viewModelScope.launch {
            productRepository.updateProduct(productId, productRequest).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _operationState.value = Resource.Success("Producto actualizado exitosamente")
                        loadProducts()
                    }
                    is Resource.Error -> {
                        _operationState.value = Resource.Error(result.message ?: "Error al actualizar producto")
                    }
                    is Resource.Loading -> {
                        _operationState.value = Resource.Loading()
                    }
                    else -> {
                        // Handle Idle or any other unhandled state
                        _operationState.value = Resource.Idle()
                    }
                }
            }
        }
    }
    

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            productRepository.deleteProduct(productId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _operationState.value = Resource.Success("Producto eliminado exitosamente")
                        loadProducts()
                    }
                    is Resource.Error -> {
                        _operationState.value = Resource.Error(result.message ?: "Error al eliminar producto")
                    }
                    is Resource.Loading -> {
                        _operationState.value = Resource.Loading()
                    }
                    else -> {
                        // Handle Idle or any other unhandled state
                        _operationState.value = Resource.Idle()
                    }
                }
            }
        }
    }
    

    fun clearOperationState() {
        _operationState.value = null
    }
    

    fun clearProductState() {
        _productState.value = null
    }
}


class AdminProductViewModelFactory(
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminProductViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

