package pe.edu.upc.parkingnow.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.upc.parkingnow.R

@Composable
fun ChangePasswordScreen(navController: NavController) {
    var newPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    var showWeakPasswordWarning by remember { mutableStateOf(false) }
    var triggerLogin by remember { mutableStateOf(false) }

    LaunchedEffect(triggerLogin) {
        if (triggerLogin) {
            kotlinx.coroutines.delay(3000)
            showConfirmation = false
            triggerLogin = false
            navController.navigate("login")
        }
    }

    LaunchedEffect(showWeakPasswordWarning) {
        if (showWeakPasswordWarning) {
            kotlinx.coroutines.delay(3000)
            showWeakPasswordWarning = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        if (showConfirmation) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 24.dp, vertical = 72.dp),
                shape = RoundedCornerShape(12.dp),
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ) {
                Text("Your password has been successfully changed.", style = MaterialTheme.typography.bodyMedium)
            }
        }

        if (showWeakPasswordWarning) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 72.dp, start = 24.dp, end = 24.dp)
                    .background(
                        color = Color(0xFFD32F2F),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Your password must have:\n• At least 8 characters\n• One uppercase letter (A–Z)\n• One number (0–9)\n• One symbol (!, @, #, \$, %, ^, &, *)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )
            }
        }

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 32.dp, start = 16.dp)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 160.dp, bottom = 100.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(60.dp))

                Text(
                    text = "Change\nyour password",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D0D0D)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "This will be your new password for future logins.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(Icons.Default.Visibility, contentDescription = null)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.5f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.3f),
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )
            }

            Button(
                onClick = {
                    if (
                        newPassword.length >= 8 &&
                        newPassword.any { it.isDigit() } &&
                        newPassword.any { it.isUpperCase() } &&
                        newPassword.any { "!@#\$%^&*".contains(it) }
                    ) {
                        showWeakPasswordWarning = false
                        showConfirmation = true
                        triggerLogin = true
                    } else {
                        showWeakPasswordWarning = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Black
                ),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text("Change Password", fontSize = 16.sp, color = Color.Black)
            }
        }
    }
}