package pe.edu.upc.parkingnow.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.LockReset
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel

@Composable
fun ChangePasswordScreen(navController: NavController, appViewModel: AppViewModel) {
    val isDarkModeEnabled by appViewModel.isDarkMode.collectAsState()
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    var showWeakPasswordWarning by remember { mutableStateOf(false) }
    var triggerLogin by remember { mutableStateOf(false) }

    // Password validation states
    val hasMinLength = newPassword.length >= 8
    val hasUpperCase = newPassword.any { it.isUpperCase() }
    val hasDigit = newPassword.any { it.isDigit() }
    val hasSpecialChar = newPassword.any { "!@#$%^&*".contains(it) }
    val passwordsMatch = newPassword == confirmPassword && newPassword.isNotEmpty()

    LaunchedEffect(hasMinLength, hasUpperCase, hasDigit, hasSpecialChar, passwordsMatch) {
        if (hasMinLength && hasUpperCase && hasDigit && hasSpecialChar && passwordsMatch) {
            showConfirmation = true
            kotlinx.coroutines.delay(3000)
            showConfirmation = false
            navController.navigate("login") {
                popUpTo("change_password") { inclusive = true }
            }
        }
    }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkModeEnabled) Color.Black else Color.White)
    ) {
        // Background image with overlay gradient
        // Image(
        //     painter = painterResource(id = R.drawable.login_background),
        //     contentDescription = null,
        //     contentScale = ContentScale.FillBounds,
        //     modifier = Modifier.fillMaxSize()
        // )

        // Semi-transparent overlay for better text readability
        // Box(
        //     modifier = Modifier
        //         .fillMaxSize()
        //         .background(
        //             Brush.verticalGradient(
        //                 colors = listOf(
        //                     Color.White.copy(alpha = 0.7f),
        //                     Color.White.copy(alpha = 0.85f)
        //                 )
        //             )
        //         )
        // )

        // Success notification
        if (showConfirmation) {
            Card(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 56.dp, top = 52.dp, end = 24.dp)
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "¡Tu contraseña ha sido cambiada exitosamente!",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Back button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF4285F4),
                modifier = Modifier.size(28.dp)
            )
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            // Main card content
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkModeEnabled) Color(0xFF1E1E1E) else Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF4285F4),
                                        Color(0xFF1976D2)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LockReset,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Title
                    Text(
                        text = "Cambiar Contraseña",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkModeEnabled) Color.White else Color(0xFF1E293B),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Subtitle
                    Text(
                        text = "Crea una nueva contraseña segura para proteger tu cuenta",
                        fontSize = 14.sp,
                        color = if (isDarkModeEnabled) Color.Gray else Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // New password input
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("Nueva contraseña") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF4285F4)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = Color(0xFF4285F4)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (isDarkModeEnabled) Color(0xFF232323) else Color.White,
                            unfocusedContainerColor = if (isDarkModeEnabled) Color(0xFF232323) else Color.White,
                            focusedBorderColor = Color(0xFF4285F4),
                            unfocusedBorderColor = Color(0xFFBDBDBD),
                            focusedTextColor = if (isDarkModeEnabled) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkModeEnabled) Color.White else Color.Black,
                            focusedLabelColor = Color(0xFF4285F4),
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm password input
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar contraseña") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF4285F4)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = Color(0xFF4285F4)
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (isDarkModeEnabled) Color(0xFF232323) else Color.White,
                            unfocusedContainerColor = if (isDarkModeEnabled) Color(0xFF232323) else Color.White,
                            focusedBorderColor = Color(0xFF4285F4),
                            unfocusedBorderColor = Color(0xFFBDBDBD),
                            focusedTextColor = if (isDarkModeEnabled) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkModeEnabled) Color.White else Color.Black,
                            focusedLabelColor = Color(0xFF4285F4),
                            unfocusedLabelColor = Color.Gray
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Password requirements
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkModeEnabled) Color(0xFF2A2A2A) else Color(0xFFF5F9FF)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Requisitos de contraseña:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isDarkModeEnabled) Color.White else Color(0xFF1E293B)
                            )

                            PasswordRequirementRow(
                                text = "Al menos 8 caracteres",
                                isMet = hasMinLength,
                                isDarkModeEnabled = isDarkModeEnabled
                            )

                            PasswordRequirementRow(
                                text = "Al menos una letra mayúscula (A-Z)",
                                isMet = hasUpperCase,
                                isDarkModeEnabled = isDarkModeEnabled
                            )

                            PasswordRequirementRow(
                                text = "Al menos un número (0-9)",
                                isMet = hasDigit,
                                isDarkModeEnabled = isDarkModeEnabled
                            )

                            PasswordRequirementRow(
                                text = "Al menos un símbolo (!@#\$%^&*)",
                                isMet = hasSpecialChar,
                                isDarkModeEnabled = isDarkModeEnabled
                            )

                            PasswordRequirementRow(
                                text = "Las contraseñas coinciden",
                                isMet = passwordsMatch,
                                isDarkModeEnabled = isDarkModeEnabled
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom help text
            Text(
                text = "¿Necesitas ayuda? Contacta a nuestro soporte",
                fontSize = 12.sp,
                color = if (isDarkModeEnabled) Color.Gray else Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PasswordRequirementRow(text: String, isMet: Boolean, isDarkModeEnabled: Boolean = false) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = if (isMet) Icons.Default.CheckCircle else Icons.Default.Cancel,
            contentDescription = null,
            tint = if (isMet) Color(0xFF4CAF50) else if (isDarkModeEnabled) Color.LightGray else Color(0xFFBDBDBD),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = if (isDarkModeEnabled) {
                if (isMet) Color(0xFF4CAF50) else Color.LightGray
            } else {
                if (isMet) Color(0xFF4CAF50) else Color.Gray
            }
        )
    }
}