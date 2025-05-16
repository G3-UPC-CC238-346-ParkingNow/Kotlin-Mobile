package pe.upc.parkingnow.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.upc.parkingnow.R // ¡Asegúrate que este import esté!

/**
 * Pantalla principal de la aplicación.
 *
 * Muestra un mensaje de bienvenida, una imagen y un botón "Empezar" que debe navegar a la pantalla de Login.
 *
 * @param onStartClick Lambda que se ejecuta al pulsar el botón "Empezar". Debe manejar la navegación hacia login.
 * @param onRegisterClick Lambda que se ejecuta al pulsar el botón "I don't have account". Debe manejar la navegación hacia registro.
 */
@Composable
fun HomeScreen(onStartClick: () -> Unit = {}, onRegisterClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Título arriba
            Text(
                text = "Tu estacionamiento ideal,\nmás cerca que nunca.",
                fontSize = 22.sp,
                color = Color(0xFF454C5E),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 18.dp)
                    .align(Alignment.CenterHorizontally),
                lineHeight = 32.sp
            )

            // Imagen central (home.png)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Imagen Home ParkingNow",
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(0.8f),
                    contentScale = ContentScale.Fit
                )
            }

            // Título y subtítulo
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ParkingNow",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF232943)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Encuentra tu espacio con un solo toque",
                    fontSize = 17.sp,
                    color = Color(0xFF838A9C)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        onStartClick() // Este evento navega al login. Ver NavGraph.
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFF500),
                        contentColor = Color(0xFF232943)
                    )
                ) {
                    Text(
                        text = "Empezar",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }
    }
}