package pe.edu.upc.parkingnow.presentation.view

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.upc.parkingnow.R
import pe.edu.upc.parkingnow.presentation.navigation.Routes

@Composable
fun TrackingScreen(navController: NavController, appViewModel: AppViewModel) {
    val context = LocalContext.current
    val isDarkMode = appViewModel.isDarkMode.collectAsState().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color(0xFF121212) else Color.White)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigate(Routes.Dashboard.route) }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = if (isDarkMode) Color.White else Color.Black
                    )
                }
            }

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
                        modifier = Modifier.align(Alignment.Center),
                        color = if (isDarkMode) Color.White else Color.Black
                    )
                }
                // Imagen decorativa a la derecha
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
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = if (isDarkMode) Color.LightGray else Color.Gray
                )

                Button(
                    onClick = {
                        Toast.makeText(context, "Monitoreo por cámara", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color(0xFF1C1C1E) else Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = null,
                        tint = if (isDarkMode) Color.LightGray else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Automóviles monitoreados por cámara", color = if (isDarkMode) Color.LightGray else Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sección 2: Últimas alertas registradas
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Últimas alertas registradas",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = if (isDarkMode) Color.LightGray else Color.Gray
                )

                Button(
                    onClick = {
                        Toast.makeText(context, "Mostrando historial de alertas", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color(0xFF1C1C1E) else Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = null,
                        tint = if (isDarkMode) Color.LightGray else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Historial de alertas", color = if (isDarkMode) Color.LightGray else Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sección 3: Explicación con tarjeta blanca
            Text(
                text = "Mantenga su carro seguro con...",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isDarkMode) Color.White else Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDarkMode) Color(0xFF1C1C1E) else Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.padding(16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Explicación de la cámara de monitoreo",
                        color = if (isDarkMode) Color.LightGray else Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja la barra inferior al fondo
        }

        // Barra de navegación inferior completamente blanca
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(if (isDarkMode) Color(0xFF1C1C1E) else Color.White)
                .padding(bottom = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (isDarkMode) Color(0xFF1C1C1E) else Color.White)
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val activeColor = Color(0xFF1D4ED8)
                val inactiveColor = Color(0xFF9CA3AF)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = { navController.navigate(Routes.Dashboard.route) }) {
                        Icon(Icons.Default.Home, contentDescription = "Inicio", tint = activeColor)
                    }
                    Text("Inicio", fontSize = 12.sp, color = activeColor)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = { navController.navigate(Routes.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = inactiveColor)
                    }
                    Text("Ajustes", fontSize = 12.sp, color = inactiveColor)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = { navController.navigate(Routes.Login.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", tint = inactiveColor)
                    }
                    Text("Perfil", fontSize = 12.sp, color = inactiveColor)
                }
            }
        }
    }
}