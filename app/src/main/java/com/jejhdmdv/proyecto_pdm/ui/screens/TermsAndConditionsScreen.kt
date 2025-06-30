package com.jejhdmdv.proyecto_pdm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pantalla de Términos y Condiciones.
 * Muestra el texto legal de los términos y condiciones de uso de la aplicación.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsAndConditionsScreen(
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Términos y Condiciones", color = Color.White) },

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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Última actualización: 28 de junio de 2025",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bienvenido a [Nombre de la Aplicación]. Al acceder o utilizar nuestra aplicación, usted acepta estar sujeto a estos Términos y Condiciones. Si no está de acuerdo con alguna parte de estos términos, no utilice nuestra aplicación.",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "1. Uso de la Aplicación",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Nuestra aplicación está diseñada para [describir el propósito de la app, ej: ayudar a los dueños de mascotas a gestionar la salud y el bienestar de sus animales]. Usted se compromete a utilizar la aplicación solo para fines lícitos y de manera que no infrinja los derechos de, restrinja o inhiba el uso y disfrute de la aplicación por parte de terceros.",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "2. Cuentas de Usuario",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Para acceder a ciertas funciones de la aplicación, es posible que deba crear una cuenta. Usted es responsable de mantener la confidencialidad de su información de cuenta y de todas las actividades que ocurran bajo su cuenta.",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "3. Contenido del Usuario",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Usted es el único responsable del contenido que publique, cargue, enlace o ponga a disposición a través de la aplicación. Nos reservamos el derecho de eliminar cualquier contenido que consideremos inapropiado o que viole estos Términos y Condiciones.",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "4. Privacidad",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Nuestra Política de Privacidad, que describe cómo recopilamos, usamos y protegemos su información personal, forma parte de estos Términos y Condiciones. Al utilizar la aplicación, usted acepta nuestra Política de Privacidad.",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "5. Limitación de Responsabilidad",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "La aplicación se proporciona 'tal cual' y 'según disponibilidad' sin garantías de ningún tipo. No seremos responsables de ningún daño directo, indirecto, incidental, especial, consecuente o punitivo que resulte del uso o la imposibilidad de usar la aplicación.",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "6. Modificaciones",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Nos reservamos el derecho de modificar o reemplazar estos Términos y Condiciones en cualquier momento. Si una revisión es material, intentaremos proporcionar un aviso de al menos 30 días antes de que los nuevos términos entren en vigencia.",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "7. Ley Aplicable",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Estos Términos y Condiciones se regirán e interpretarán de acuerdo con las leyes de [Su País/Estado], sin tener en cuenta sus disposiciones sobre conflicto de leyes.",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "8. Contacto",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Si tiene alguna pregunta sobre estos Términos y Condiciones, contáctenos en [su correo electrónico de contacto].",
                fontSize = 14.sp,
                color = Color.Black,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TermsAndConditionsScreenPreview() {
    TermsAndConditionsScreen()
}
