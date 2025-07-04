package com.jejhdmdv.proyecto_pdm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jejhdmdv.proyecto_pdm.data.repository.admin.ProductRepository
import com.jejhdmdv.proyecto_pdm.model.admin.Product
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de tienda (usuarios finales)
 * Maneja la carga de productos activos y funcionalidades de búsqueda/filtrado
 */
class StoreViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {
    
    // Estado de los productos
    private val _productsState = MutableStateFlow<Resource<List<Product>>>(Resource.Loading())
    val productsState: StateFlow<Resource<List<Product>>> = _productsState.asStateFlow()
    
    // Query de búsqueda
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // Categoría seleccionada
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()
    
    // Productos filtrados
    val filteredProducts: StateFlow<List<Product>> = combine(
        _productsState,
        _searchQuery,
        _selectedCategory
    ) { productsResource, query, category ->
        when (productsResource) {
            is Resource.Success -> {
                val products = productsResource.data ?: emptyList()
                products.filter { product ->
                    val matchesSearch = if (query.isBlank()) true else
                        product.name.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true)
                    val matchesCategory = category?.let { product.category == it } ?: true
                    val isActive = product.isActive
                    
                    matchesSearch && matchesCategory && isActive
                }
            }
            else -> emptyList()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    // Categorías disponibles
    val availableCategories: StateFlow<List<String>> = _productsState.map { productsResource ->
        when (productsResource) {
            is Resource.Success -> {
                val products = productsResource.data ?: emptyList()
                products.filter { it.isActive }
                    .map { it.category }
                    .distinct()
                    .sorted()
            }
            else -> emptyList()
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    init {
        loadProducts()
    }
    
    //caragr producto activo
    fun loadProducts() {
        viewModelScope.launch {
            productRepository.getActiveProducts().collect { result ->
                _productsState.value = result
            }
        }
    }
    
    //actualiza query de bsqueda
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    //selecciona categoria
    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }
    
   //limpiar filtro
    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
    }
    

    fun refresh() {
        loadProducts()
    }
}


 //Factory para crear el StoreViewModel

class StoreViewModelFactory(
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoreViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

