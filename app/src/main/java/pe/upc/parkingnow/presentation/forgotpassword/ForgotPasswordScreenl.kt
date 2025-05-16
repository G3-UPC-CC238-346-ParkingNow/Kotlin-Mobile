package pe.upc.parkingnow.presentation.forgotpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit = {},
    onContinue: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
    ) {
        // Círculo decorativo
        Box(
            modifier = Modifier
                .size(110.dp)
                .offset(x = 140.dp, y = 5.dp)
                .align(Alignment.TopEnd)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF6B7AFF), Color(0xFF5468FF)),
                        radius = 95f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // Icono back
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF232943)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Forgot\npassword",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFF232943),
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Please enter your phone number to change your password",
                fontSize = 15.sp,
                color = Color(0xFF676D7C)
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Correo electronico",
                color = Color(0xFF232943),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = "Correo",
                        tint = Color(0xFF5468FF)
                    )
                },
                placeholder = { Text("ejemplo@gmail.com") },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(34.dp))

            Button(
                onClick = {
                    onContinue(email) // Navega a la pantalla de reset password (ver NavGraph)
                    email = "" // Limpiar campo por UX
                    // La navegación real a "reset password" se debe pasar desde el NavGraph como: onContinue = { navController.navigate("reset") }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF232943),
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Continue",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}