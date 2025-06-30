package com.jejhdmdv.proyecto_pdm


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jejhdmdv.proyecto_pdm.ui.screens.MainCalendario
import com.jejhdmdv.proyecto_pdm.ui.theme.ProyectoPDMTheme
import com.jejhdmdv.proyecto_pdm.ui.viewmodels.calendarioviewmodel.ReminderViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyectoPDMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val viewModel: ReminderViewModel = viewModel()

                    MainCalendario(viewModel = viewModel)
                }
            }
        }
    }
}

