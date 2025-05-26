package pe.edu.upc.parkingnow.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import pe.edu.upc.parkingnow.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SuccessScreen(navController: NavController) {
    LaunchedEffect(true) {
        delay(3000)
        navController.navigate("bookings")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEF3FF)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.check),
                contentDescription = "Success Check",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Success!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Yey, 1 parking slot\nalready booked for you.", fontSize = 14.sp, color = Color(0xFF444444))
        }
    }
}
