package com.jejhdmdv.proyecto_pdm.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



 //Data class para representar una pregunta frecuente (solo como guia para la screen).

data class FAQItem(
    val question: String,
    val answer: String
)

/*
 Pantalla de Preguntas Frecuentes.
 Muestra una lista de preguntas y respuestas en un formato de cards expandibles.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQScreen(
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Preguntas Frecuentes", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFCEB6B6))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            val faqItems = remember { getSampleFAQItems() }

            LazyColumn {
                items(faqItems) { faqItem ->
                    FAQCard(faqItem = faqItem)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}


 //Card individual para una pregunta frecuente, con funcionalidad de expandir/colapsar.

@Composable
fun FAQCard(
    faqItem: FAQItem,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = faqItem.question,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Colapsar" else "Expandir",
                        tint = Color.DarkGray
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = faqItem.answer,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}


  //Función para obtener datos de ejemplo de preguntas frecuentes.

private fun getSampleFAQItems(): List<FAQItem> {
    return listOf(
        FAQItem(
            question = "¿Cómo registro a mi mascota en la aplicación?",
            answer = "Para registrar a tu mascota, ve a la sección 'Registro de Mascota' en el menú principal, selecciona el tipo de mascota y rellena los datos solicitados."
        ),
        FAQItem(
            question = "¿Puedo agendar citas veterinarias desde la app?",
            answer = "Sí, puedes agendar citas directamente desde la sección 'Gestión de Citas'. Selecciona la fecha y hora que mejor te convenga."
        ),
        FAQItem(
            question = "¿Cómo cambio el modo de visualización (claro/oscuro)?",
            answer = "Puedes cambiar el modo de visualización en 'Preferencias del Sistema' dentro de la sección de Ajustes. Selecciona 'Modo Claro' o 'Modo Oscuro'."
        ),
        FAQItem(
            question = "¿Qué hago si mi mascota tiene una emergencia?",
            answer = "En caso de emergencia, dirígete a la sección 'Emergencias' donde encontrarás información de contacto de clínicas veterinarias cercanas y un botón para llamar directamente."
        ),
        FAQItem(
            question = "¿Cómo reporto un problema con la aplicación?",
            answer = "Si encuentras algún problema, ve a 'Reportar un Problema' en Ajustes. Podrás seleccionar un problema frecuente o describir uno nuevo."
        )
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FAQScreenPreview() {
    FAQScreen()
}
