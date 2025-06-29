package com.jejhdmdv.proyecto_pdm.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class ProductDetail(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val imageRes: Int? = null,
    val category: ProductCategory
)


@Composable
fun ProductDetailScreen(
    productId: String,
    onNavigateBack: () -> Unit = {},
    onAddToCart: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    productDetail: ProductDetail? = getSampleProductDetail(productId)
) {
    Scaffold(
        topBar = {
            ProductDetailTopBar(
                onNavigateBack = onNavigateBack,
                productName = productDetail?.name ?: "Producto"
            )
        }
    ) { paddingValues ->
        if (productDetail != null) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen del producto
                ProductImageSection(
                    imageRes = productDetail.imageRes,
                    productName = productDetail.name
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Información del producto
                ProductInfoSection(
                    productDetail = productDetail,
                    onNavigateBack = onNavigateBack,
                    onAddToCart = { onAddToCart(productDetail.id) }
                )
                
                Spacer(modifier = Modifier.height(100.dp)) 
            }
        } else {
            // Producto no encontrado
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Producto no encontrado",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(onClick = onNavigateBack) {
                        Text("Regresar")
                    }
                }
            }
        }
    }
}

//barra superior del detalle del producto
@Composable
private fun ProductDetailTopBar(
    onNavigateBack: () -> Unit,
    productName: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF73A5A7))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.White
                )
            }
            
            Text(
                text = productName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
            

            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

//imagen del producto
@Composable
private fun ProductImageSection(
    imageRes: Int?,
    productName: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        if (imageRes != null) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = productName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            // Placeholder para imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Imagen del producto",
                        modifier = Modifier.size(64.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Imagen del producto",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

//informacion del producto
@Composable
private fun ProductInfoSection(
    productDetail: ProductDetail,
    onNavigateBack: () -> Unit,
    onAddToCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Precio del producto
        Text(
            text = "$${String.format("%.2f", productDetail.price)}",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF73A5A7),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Descripción del producto
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Descripción",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = productDetail.description,
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    lineHeight = 24.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botón regresar
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF73A5A7)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Regresar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Botón añadir al carrito
            Button(
                onClick = onAddToCart,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF73A5A7),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Añadir al carrito",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

//funcion para tener detalles del producto de muestra
private fun getSampleProductDetail(productId: String): ProductDetail? {
    val sampleProducts = mapOf(
        "1" to ProductDetail(
            id = "1",
            name = "Comida para Perro Premium",
            price = 25.99,
            description = "Alimento balanceado premium para perros adultos. Contiene proteínas de alta calidad, vitaminas y minerales esenciales para mantener a tu mascota saludable y activa. Ingredientes naturales sin conservantes artificiales.",
            imageRes = null,
            category = ProductCategory.DOGS
        ),
        "2" to ProductDetail(
            id = "2",
            name = "Comida para Gato",
            price = 22.50,
            description = "Alimento completo y balanceado para gatos adultos. Rico en proteínas y con el sabor que más les gusta. Ayuda a mantener un pelaje brillante y una digestión saludable.",
            imageRes = null,
            category = ProductCategory.CATS
        ),
        "3" to ProductDetail(
            id = "3",
            name = "Juguete para Perro",
            price = 15.99,
            description = "Juguete resistente y seguro para perros de todas las edades. Ayuda a mantener los dientes limpios y proporciona horas de entretenimiento. Material no tóxico y duradero.",
            imageRes = null,
            category = ProductCategory.TOYS
        ),
        "4" to ProductDetail(
            id = "4",
            name = "Collar para Perro",
            price = 18.75,
            description = "Collar ajustable y cómodo para perros. Fabricado con materiales de alta calidad que garantizan durabilidad y comodidad. Disponible en varios tamaños.",
            imageRes = null,
            category = ProductCategory.ACCESSORIES
        ),
        "5" to ProductDetail(
            id = "5",
            name = "Arena para Gato",
            price = 12.99,
            description = "Arena aglomerante de alta absorción para gatos. Control de olores superior y fácil limpieza. Libre de polvo y segura para tu mascota.",
            imageRes = null,
            category = ProductCategory.CATS
        ),
        "6" to ProductDetail(
            id = "6",
            name = "Pelota Interactiva",
            price = 8.50,
            description = "Pelota interactiva que estimula el juego y el ejercicio. Perfecta para perros y gatos activos. Material resistente y colores llamativos.",
            imageRes = null,
            category = ProductCategory.TOYS
        )
    )
    
    return sampleProducts[productId]
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen(productId = "1")
}

