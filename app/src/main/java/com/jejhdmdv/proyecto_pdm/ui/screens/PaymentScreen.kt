package com.jejhdmdv.proyecto_pdm.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//representacion de pagos
data class PaymentData(
    val cardHolderName: String,
    val cardNumber: String,
    val expirationDate: String,
    val cvv: String,
    val address: String
)

/**
 * Pantalla de procesamiento de pago
 * Permite al usuario ingresar los datos de su tarjeta y dirección para completar la compra
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onNavigateBack: () -> Unit = {},
    onProcessPayment: (PaymentData) -> Unit = {},
    totalAmount: Double = 0.0,
    modifier: Modifier = Modifier
) {
    // Estados para los campos del formulario
    var cardHolderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    // Estados para validación
    var isFormValid by remember { mutableStateOf(false) }

    // Validar formulario
    isFormValid = cardHolderName.isNotBlank() &&
            cardNumber.length >= 16 &&
            expirationDate.length >= 5 &&
            cvv.length >= 3 &&
            address.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Procesar Pago",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF73A5A7)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Resumen del total
            PaymentSummaryCard(totalAmount = totalAmount)

            Spacer(modifier = Modifier.height(24.dp))

            // Formulario de datos de tarjeta
            PaymentFormCard(
                cardHolderName = cardHolderName,
                onCardHolderNameChange = { cardHolderName = it },
                cardNumber = cardNumber,
                onCardNumberChange = { 
                    // Limitar a 16 dígitos y solo números
                    if (it.length <= 16 && it.all { char -> char.isDigit() }) {
                        cardNumber = it
                    }
                },
                expirationDate = expirationDate,
                onExpirationDateChange = { 
                    // Formato MM/YY
                    val filtered = it.filter { char -> char.isDigit() }
                    val formatted = when {
                        filtered.length <= 2 -> filtered
                        filtered.length <= 4 -> "${filtered.substring(0, 2)}/${filtered.substring(2)}"
                        else -> "${filtered.substring(0, 2)}/${filtered.substring(2, 4)}"
                    }
                    if (formatted.length <= 5) {
                        expirationDate = formatted
                    }
                },
                cvv = cvv,
                onCvvChange = { 
                    // Limitar a 3 dígitos
                    if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                        cvv = it
                    }
                },
                address = address,
                onAddressChange = { address = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de procesar pago
            ProcessPaymentButton(
                isEnabled = isFormValid,
                totalAmount = totalAmount,
                onProcessPayment = {
                    val paymentData = PaymentData(
                        cardHolderName = cardHolderName,
                        cardNumber = cardNumber,
                        expirationDate = expirationDate,
                        cvv = cvv,
                        address = address
                    )
                    onProcessPayment(paymentData)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Card con el resumen del pago
 */
@Composable
private fun PaymentSummaryCard(
    totalAmount: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total a Pagar",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "$${String.format("%.2f", totalAmount)}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF73A5A7),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Card con el formulario de datos de pago
 */
@Composable
private fun PaymentFormCard(
    cardHolderName: String,
    onCardHolderNameChange: (String) -> Unit,
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    expirationDate: String,
    onExpirationDateChange: (String) -> Unit,
    cvv: String,
    onCvvChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Datos de la Tarjeta",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Nombre del titular
            OutlinedTextField(
                value = cardHolderName,
                onValueChange = onCardHolderNameChange,
                label = { Text("Nombre del Titular") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Nombre",
                        tint = Color(0xFF73A5A7)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Número de tarjeta
            OutlinedTextField(
                value = cardNumber,
                onValueChange = onCardNumberChange,
                label = { Text("Número de Tarjeta") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = "Tarjeta",
                        tint = Color(0xFF73A5A7)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                placeholder = { Text("1234 5678 9012 3456") },
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fecha de expiración y CVV
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Fecha de expiración
                OutlinedTextField(
                    value = expirationDate,
                    onValueChange = onExpirationDateChange,
                    label = { Text("MM/YY") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    placeholder = { Text("12/25") },
                    shape = RoundedCornerShape(8.dp)
                )

                // CVV
                OutlinedTextField(
                    value = cvv,
                    onValueChange = onCvvChange,
                    label = { Text("CVV") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    placeholder = { Text("123") },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Dirección de Facturación",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Dirección
            OutlinedTextField(
                value = address,
                onValueChange = onAddressChange,
                label = { Text("Dirección Completa") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Dirección",
                        tint = Color(0xFF73A5A7)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 3,
                placeholder = { Text("Calle, número, colonia, ciudad, código postal") },
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

/**
 * Botón para procesar el pago
 */
@Composable
private fun ProcessPaymentButton(
    isEnabled: Boolean,
    totalAmount: Double,
    onProcessPayment: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onProcessPayment,
            enabled = isEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF73A5A7),
                disabledContainerColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Pagar $${String.format("%.2f", totalAmount)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentScreenPreview() {
    PaymentScreen(
        totalAmount = 125.50
    )
}

