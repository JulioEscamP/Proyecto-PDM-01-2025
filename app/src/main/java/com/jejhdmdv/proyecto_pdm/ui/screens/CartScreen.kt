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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Data class para representar un item en el carrito
 */
data class CartItem(
    val productId: String,
    val productName: String,
    val price: Double,
    val quantity: Int,
    val imageRes: Int? = null
)

/**
 * Pantalla del carrito de compras
 * Muestra los productos seleccionados con opciones para modificar cantidad y eliminar
 */
@Composable
fun CartScreen(
    onNavigateBack: () -> Unit = {},
    onCheckout: (Double) -> Unit = {},
    modifier: Modifier = Modifier,
    initialCartItems: List<CartItem> = getSampleCartItems()
) {
    var cartItems by remember { mutableStateOf(initialCartItems) }

    // Calcular total
    val totalAmount = cartItems.sumOf { it.price * it.quantity }

    Scaffold(
        topBar = {
            CartTopBar(
                onNavigateBack = onNavigateBack,
                itemCount = cartItems.sumOf { it.quantity }
            )
        }
    ) { paddingValues ->
        if (cartItems.isEmpty()) {
            // Carrito vacío
            EmptyCartContent(
                onNavigateBack = onNavigateBack,
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            // Carrito con productos
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                // Lista de productos en el carrito
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(cartItems) { cartItem ->
                        CartItemCard(
                            cartItem = cartItem,
                            onQuantityChange = { newQuantity ->
                                cartItems = cartItems.map { item ->
                                    if (item.productId == cartItem.productId) {
                                        item.copy(quantity = newQuantity)
                                    } else item
                                }
                            },
                            onRemoveItem = {
                                cartItems = cartItems.filter { it.productId != cartItem.productId }
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // Sección de total y checkout
                CartSummarySection(
                    totalAmount = totalAmount,
                    onCheckout = { onCheckout(totalAmount) }
                )
            }
        }
    }
}

/*
  Barra superior del carrito
 */
@Composable
private fun CartTopBar(
    onNavigateBack: () -> Unit,
    itemCount: Int
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
                text = "Carrito ($itemCount)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )

            // Espacio para mantener el título centrado
            Spacer(modifier = Modifier.size(48.dp))
        }
    }
}

/*
  Contenido cuando el carrito está vacío
 */
@Composable
private fun EmptyCartContent(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Carrito vacío",
                modifier = Modifier.size(80.dp),
                tint = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tu carrito está vacío",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Agrega productos para comenzar",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onNavigateBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF73A5A7),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Ir a la tienda",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/*
  Card de item en el carrito
 */
@Composable
private fun CartItemCard(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveItem: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto//
            if (cartItem.imageRes != null) {
                Image(
                    painter = painterResource(id = cartItem.imageRes),
                    contentDescription = cartItem.productName,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder para imagen
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Imagen del producto",
                        modifier = Modifier.size(32.dp),
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información del producto
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.productName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$${String.format("%.2f", cartItem.price)}",
                    fontSize = 14.sp,
                    color = Color(0xFF73A5A7),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Controles de cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón disminuir
                    IconButton(
                        onClick = {
                            if (cartItem.quantity > 1) {
                                onQuantityChange(cartItem.quantity - 1)
                            }
                        },
                        enabled = cartItem.quantity > 1
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Disminuir cantidad",
                            tint = if (cartItem.quantity > 1) Color(0xFF73A5A7) else Color.Gray
                        )
                    }

                    Text(
                        text = cartItem.quantity.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // Botón aumentar
                    IconButton(
                        onClick = {
                            onQuantityChange(cartItem.quantity + 1)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Aumentar cantidad",
                            tint = Color(0xFF73A5A7)
                        )
                    }
                }
            }

            // Botón eliminar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = onRemoveItem
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar producto",
                        tint = Color.Red
                    )
                }

                Text(
                    text = "$${String.format("%.2f", cartItem.price * cartItem.quantity)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }
    }
}

/*
  Sección de resumen de compra y checkout
 */
@Composable
private fun CartSummarySection(
    totalAmount: Double,
    onCheckout: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Resumen del pedido",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = Color.LightGray)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total a pagar:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Text(
                    text = "$${String.format("%.2f", totalAmount)}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF73A5A7)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onCheckout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF73A5A7),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Pagar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

/*
  Función para obtener items de carrito de muestra
 */
private fun getSampleCartItems(): List<CartItem> {
    return listOf(
        CartItem(
            productId = "1",
            productName = "Comida para Perro Premium",
            price = 25.99,
            quantity = 2,
            imageRes = null
        ),
        CartItem(
            productId = "3",
            productName = "Juguete para Perro",
            price = 15.99,
            quantity = 1,
            imageRes = null
        ),
        CartItem(
            productId = "5",
            productName = "Arena para Gato",
            price = 12.99,
            quantity = 3,
            imageRes = null
        )
    )
}

//carrito con compra
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenPreview() {
    CartScreen()
}

//para el carrito vacio
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EmptyCartScreenPreview() {
    CartScreen(initialCartItems = emptyList())
}

