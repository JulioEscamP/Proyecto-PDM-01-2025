package com.jejhdmdv.proyecto_pdm.ui.admin.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.model.admin.Product
import com.jejhdmdv.proyecto_pdm.model.admin.ProductRequest

//pantalla para añadir o editar producto s
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductFormScreen(
    product: Product? = null,
    isLoading: Boolean,
    onNavigateBack: () -> Unit,
    onSaveProduct: (ProductRequest) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue(product?.name ?: "")) }
    var price by remember { mutableStateOf(TextFieldValue(product?.price?.toString() ?: "")) }
    var category by remember { mutableStateOf(TextFieldValue(product?.category ?: "")) }
    var description by remember { mutableStateOf(TextFieldValue(product?.description ?: "")) }
    var imageUrl by remember { mutableStateOf(TextFieldValue(product?.imageUrl ?: "")) }
    var isActive by remember { mutableStateOf(product?.isActive ?: true) }
    
    var nameError by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }
    
    val isEditing = product != null
    
    fun validateForm(): Boolean {
        nameError = if (name.text.isBlank()) "El nombre es requerido" else ""
        priceError = if (price.text.isBlank()) "El precio es requerido" 
                    else if (price.text.toDoubleOrNull() == null || price.text.toDouble() < 0) "Precio inválido" 
                    else ""
        categoryError = if (category.text.isBlank()) "La categoría es requerida" else ""
        
        return nameError.isEmpty() && priceError.isEmpty() && categoryError.isEmpty()
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text(if (isEditing) "Editar Producto" else "Nuevo Producto") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Formulario
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información del Producto",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    // Campo Nombre
                    OutlinedTextField(
                        value = name,
                        onValueChange = { 
                            name = it
                            nameError = ""
                        },
                        label = { Text("Nombre del producto *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = nameError.isNotEmpty(),
                        supportingText = if (nameError.isNotEmpty()) {
                            { Text(nameError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        leadingIcon = {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Campo Precio
                    OutlinedTextField(
                        value = price,
                        onValueChange = { 
                            price = it
                            priceError = ""
                        },
                        label = { Text("Precio *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = priceError.isNotEmpty(),
                        supportingText = if (priceError.isNotEmpty()) {
                            { Text(priceError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        leadingIcon = {
                            Icon(Icons.Default.AttachMoney, contentDescription = null)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Campo Categoría
                    OutlinedTextField(
                        value = category,
                        onValueChange = { 
                            category = it
                            categoryError = ""
                        },
                        label = { Text("Categoría *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = categoryError.isNotEmpty(),
                        supportingText = if (categoryError.isNotEmpty()) {
                            { Text(categoryError, color = MaterialTheme.colorScheme.error) }
                        } else null,
                        leadingIcon = {
                            Icon(Icons.Default.Category, contentDescription = null)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Campo URL de imagen
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("URL de la imagen") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Image, contentDescription = null)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Campo Descripción
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(Icons.Default.Description, contentDescription = null)
                        },
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Switch Estado Activo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Producto activo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Switch(
                            checked = isActive,
                            onCheckedChange = { isActive = it }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Botón Guardar
            Button(
                onClick = {
                    if (validateForm()) {
                        val productRequest = ProductRequest(
                            name = name.text.trim(),
                            price = price.text.toDouble(),
                            category = category.text.trim(),
                            description = description.text.trim(),
                            imageUrl = imageUrl.text.trim(),
                            isActive = isActive
                        )
                        onSaveProduct(productRequest)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEditing) "Actualizar Producto" else "Crear Producto",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Nota sobre campos requeridos
            Text(
                text = "* Campos requeridos",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminProductFormScreenPreview() {
    AdminProductFormScreen(
        product = null,
        isLoading = false,
        onNavigateBack = {},
        onSaveProduct = {}
    )
}

@Preview(showBackground = true)
@Composable
fun AdminProductFormScreenEditPreview() {
    val sampleProduct = Product(
        id = "1",
        name = "Comida para Perros Premium",
        category = "Alimentación",
        price = 25.99,
        isActive = true,
        description = "Comida premium para perros adultos con ingredientes naturales",
        imageUrl = "https://example.com/image.jpg"
    )
    
    AdminProductFormScreen(
        product = sampleProduct,
        isLoading = false,
        onNavigateBack = {},
        onSaveProduct = {}
    )
}

