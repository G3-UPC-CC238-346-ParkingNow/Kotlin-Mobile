package pe.upc.parkingnow.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    // Estados de campos y password visible
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 28.dp, vertical = 20.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White.copy(alpha = 0.98f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Circulo decorativo superior
            Box(
                Modifier
                    .size(90.dp)
                    .offset(y = (-40).dp)
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF6B7AFF), Color(0xFF5468FF)),
                            radius = 80f
                        ),
                        CircleShape
                    )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Login to\nyour account",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFF232943),
                modifier = Modifier.align(Alignment.Start),
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Email
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
                label = { Text("Correo electronico") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
            )

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Lock,
                        contentDescription = "Contraseña",
                        tint = Color(0xFF5468FF)
                    )
                },
                placeholder = { Text("**********") },
                label = { Text("Password") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Ocultar" else "Mostrar"
                        )
                    }
                }
            )

            // Forgot password
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, end = 4.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Forgot password?",
                    fontSize = 15.sp,
                    color = Color(0xFF5468FF),
                    modifier = Modifier.clickable {
                        onForgotPassword() // Navega a la pantalla ForgotPassword (ver NavGraph)
                    }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Botón Login
            Button(
                onClick = { onLoginSuccess() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF232943),
                    contentColor = Color.White
                )
            ) {
                Text(
                    "Login",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Link de registro
            TextButton(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth()) {
                Text(
                    "I don't have account",
                    color = Color(0xFF5468FF),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}