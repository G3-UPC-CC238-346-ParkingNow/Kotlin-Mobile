package pe.edu.upc.parkingnow.presentation.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.collectAsState
import pe.edu.upc.parkingnow.R
import pe.edu.upc.parkingnow.presentation.navigation.Routes

@Composable
fun NotificationsScreen(navController: NavController, appViewModel: AppViewModel) {
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = if (isDarkMode) Color.White else Color.Black
                    )
                }
            }
            Text(
                text = "Notificaciones",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) Color.White else Color.Black
            )

            Spacer(modifier = Modifier.height(48.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Recibos",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDarkMode) Color.White else Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Button(
                    onClick = {
                        Toast.makeText(context, "Mostrando recibos", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color(0xFF1C1C1E) else Color.White)
                ) {
                    Icon(Icons.Default.Receipt, contentDescription = null, tint = if (isDarkMode) Color.LightGray else Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver recibos", color = if (isDarkMode) Color.LightGray else Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Notificaciones de lugares favoritos",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDarkMode) Color.White else Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Button(
                    onClick = {
                        Toast.makeText(context, "Mostrando notificaciones favoritas", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (isDarkMode) Color(0xFF1C1C1E) else Color.White)
                ) {
                    Icon(Icons.Default.Place, contentDescription = null, tint = if (isDarkMode) Color.LightGray else Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver favoritos", color = if (isDarkMode) Color.LightGray else Color.Gray)
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Barra de navegación inferior compatible con modo oscuro
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = { navController.navigate(Routes.Dashboard.route) }) {
                        Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color(0xFF1D4ED8))
                    }
                    Text("Inicio", fontSize = 12.sp, color = Color(0xFF1D4ED8))
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = { navController.navigate(Routes.Settings.route) }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = if (isDarkMode) Color.LightGray else Color.Gray)
                    }
                    Text("Ajustes", fontSize = 12.sp, color = if (isDarkMode) Color.LightGray else Color.Gray)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(onClick = { navController.navigate(Routes.Login.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", tint = if (isDarkMode) Color.LightGray else Color.Gray)
                    }
                    Text("Perfil", fontSize = 12.sp, color = if (isDarkMode) Color.LightGray else Color.Gray)
                }
            }
        }
    }
}