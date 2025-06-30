package com.jejhdmdv.proyecto_pdm.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
// import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jejhdmdv.proyecto_pdm.R
import com.jejhdmdv.proyecto_pdm.ui.components.BottomNavBar


data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageRes: Int? = null,
    val category: ProductCategory
)

//para las categorias dedl producto
enum class ProductCategory(val displayName: String, val iconResId: Int) {
    DOGS("Perros", R.drawable.perrito_icon),
    CATS("Gatos", R.drawable.gatito_icon),
    TOYS("Juguetes", R.drawable.juguetes_icon),
    ACCESSORIES("Accesorios", R.drawable.accesorios_icon)
}

/**
 * Pantalla principal de la tienda
 */
@Composable
fun StoreScreen(
    onNavigateToAppointments: () -> Unit = {},
    onNavigateToEmergency: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToProductDetail: (String) -> Unit = {},
    onNavigateToCart: () -> Unit = {},
    modifier: Modifier = Modifier,
    products: List<Product> = sampleProducts()
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }

    // Filtrar productos basado en búsqueda y categoría
    val filteredProducts = products.filter { product ->
        val matchesSearch = if (searchQuery.isBlank()) true else
            product.name.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory?.let { product.category == it } ?: true
        matchesSearch && matchesCategory
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "store",
                onNavigateToHome = {  },
                onNavigateToEmergency = onNavigateToEmergency,
                onNavigateToAppointments = onNavigateToAppointments,
                onNavigateToStore = {  },
                onNavigateToSettings = onNavigateToSettings
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // Header con título, carrito y campo de búsqueda (FIJO)
            StoreHeader(
                onNavigateToCart = onNavigateToCart,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )

            // Contenido desplazable (Categorías, Título de productos y Grid de productos)
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Categorías
                    CategorySection(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { category ->
                            selectedCategory = if (selectedCategory == category) null else category
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Título de productos
                    Text(
                        text = "Productos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

               //para el desplazable
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredProducts) { product ->
                            ProductCard(
                                product = product,
                                onProductClick = { onNavigateToProductDetail(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Header de la tienda con título, carrito y campo de búsqueda
 */
@Composable
private fun StoreHeader(
    onNavigateToCart: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF73A5A7))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "PET CARE+",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            IconButton(onClick = onNavigateToCart) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito de compras",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Campo de búsqueda dentro del header
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Buscar", color = Color.White.copy(alpha = 0.7f)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color.White
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            singleLine = true,
            shape = RoundedCornerShape(8.dp)

        )
    }
}

/**
 * Sección de categorías
 */
@Composable
private fun CategorySection(
    selectedCategory: ProductCategory?,
    onCategorySelected: (ProductCategory) -> Unit
) {
    Column {
        Text(
            text = "Categoría",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(ProductCategory.values()) { category ->
                CategoryCard(
                    category = category,
                    isSelected = selectedCategory == category,
                    onCategoryClick = { onCategorySelected(category) }
                )
            }
        }
    }
}

/**
 * Card de categoría
 */
@Composable
private fun CategoryCard(
    category: ProductCategory,
    isSelected: Boolean,
    onCategoryClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onCategoryClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF73A5A7) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de categoría
            Image(
                painter = painterResource(id = category.iconResId),
                contentDescription = category.displayName,
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = category.displayName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else Color.Black,
                textAlign = TextAlign.Start
            )
        }
    }
}

/**
 * Card de producto
 */
@Composable
private fun ProductCard(
    product: Product,
    onProductClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Imagen del producto
            if (product.imageRes != null) {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder para imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Imagen del producto",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Precio del producto
            Text(
                text = "$${String.format("%.2f", product.price)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF73A5A7),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Función para generar productos de muestra
 */
private fun sampleProducts(): List<Product> {
    return listOf(
        Product("1", "Comida Perro", 25.99, null, ProductCategory.DOGS),
        Product("2", "Comida Gato", 22.50, null, ProductCategory.CATS),
        Product("3", "Juguete Perro", 15.99, null, ProductCategory.TOYS),
        Product("4", "Collar Perro", 18.75, null, ProductCategory.ACCESSORIES),
        Product("5", "Arena Gato", 12.99, null, ProductCategory.CATS),
        Product("6", "Pelota", 8.50, null, ProductCategory.TOYS)
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StoreScreenPreview() {
    StoreScreen()
}
