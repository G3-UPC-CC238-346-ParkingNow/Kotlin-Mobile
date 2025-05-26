
package pe.edu.upc.parkingnow.presentation.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TrackingScreen(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .padding(24.dp)
    ) {
        // Header: Título centrado y imagen decorativa a la derecha
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = "Seguimiento",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            // Imagen decorativa a la derecha
            // Reemplazar "R.drawable.ic_tracking_decor" por el recurso real
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
                contentDescription = "Decoración",
                modifier = Modifier
                    .size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Sección 1: Automóviles registrados
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Automóviles registrados",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    Toast.makeText(context, "Monitoreo por cámara", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Automóviles monitoreados por cámara", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Sección 2: Últimas alertas registradas
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Últimas alertas registradas",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    Toast.makeText(context, "Mostrando historial de alertas", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Historial de alertas", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Sección 3: Explicación con tarjeta blanca
        Text(
            text = "Mantenga su carro seguro con...",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Explicación de la cámara de monitoreo",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

