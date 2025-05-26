package pe.edu.upc.parkingnow.presentation.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BookingsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Reservas",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Imagen de usuario (temporal con ícono por ahora)
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "User",
            tint = Color(0xFF0A4FFF),
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Reserva activas",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Button(
            onClick = { /* Acción registro de reservas activas */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Registro de reservas activas - lista", color = Color.Black)
        }

        Text(
            text = "¿Desea reservar algo?",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp)
        )

        Button(
            onClick = { /* Acción ver historial */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("Historial", color = Color.Black)
        }

        Text(
            text = "Lista de lugares favoritos",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 16.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color.White, shape = RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {
            Text("Lugares guardados por el usuario", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate("ticket")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A4FFF))
        ) {
            Text("Realizar una reserva", color = Color.White, fontSize = 16.sp)
        }
    }
}