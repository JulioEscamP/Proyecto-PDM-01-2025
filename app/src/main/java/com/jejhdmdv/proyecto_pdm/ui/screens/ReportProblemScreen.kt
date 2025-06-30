package com.jejhdmdv.proyecto_pdm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color // Asegúrate de que esta importación esté presente
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class ProblemItem(
    val id: String,
    val description: String
)

/**
 * Pantalla para Reportar un Problema.
 * Permite al usuario seleccionar problemas frecuentes o describir uno nuevo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportProblemScreen(
    onNavigateBack: () -> Unit = {},
    onReportProblem: (List<String>, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var selectedProblems by remember { mutableStateOf(setOf<String>()) }
    var otherProblemText by remember { mutableStateOf("") }

    val frequentProblems = remember { getFrequentProblems() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportar un Problema", color = Color.White) },

                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF73A5A7))
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

            Text(
                text = "Selecciona los problemas que estás experimentando o describe uno nuevo:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Lista de problemas frecuentes
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(frequentProblems) { problem ->
                    ProblemCheckboxItem(
                        problem = problem,
                        isChecked = problem.id in selectedProblems,
                        onCheckedChange = {
                            selectedProblems = if (it) {
                                selectedProblems + problem.id
                            } else {
                                selectedProblems - problem.id
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para otros problemas
            OutlinedTextField(
                value = otherProblemText,
                onValueChange = { otherProblemText = it },
                label = { Text("Describe otro problema (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.Gray
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de enviar
            Button(
                onClick = { onReportProblem(selectedProblems.toList(), otherProblemText) },
                enabled = selectedProblems.isNotEmpty() || otherProblemText.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF73A5A7),
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray.copy(alpha = 0.6f),
                    disabledContentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Enviar Reporte",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


 //Elemento de checkbox para un problema frecuente.

@Composable
private fun ProblemCheckboxItem(
    problem: ProblemItem,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) },
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = Color.Gray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = problem.description,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

/**
 * Función para obtener una lista de problemas frecuentes de ejemplo.
 */
private fun getFrequentProblems(): List<ProblemItem> {
    return listOf(
        ProblemItem("login_issue", "No puedo iniciar sesión"),
        ProblemItem("app_crashes", "La aplicación se cierra inesperadamente"),
        ProblemItem("data_not_loading", "Los datos no se cargan correctamente"),
        ProblemItem("notifications_issue", "No recibo notificaciones"),
        ProblemItem("ui_glitch", "Problemas con la interfaz de usuario"),
        ProblemItem("other", "Otro (describir abajo)")
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReportProblemScreenPreview() {
    ReportProblemScreen()
}
