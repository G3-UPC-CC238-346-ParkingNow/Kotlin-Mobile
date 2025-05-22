package pe.edu.upc.parkingnow.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import pe.edu.upc.parkingnow.presentation.navigation.Routes

@Composable
fun WelcomeScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(3000) // Espera de 3 segundos
        navController.navigate(Routes.Login.route) {
            popUpTo(Routes.Welcome.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "ParkingNow",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}