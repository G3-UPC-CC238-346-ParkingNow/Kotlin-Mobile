package pe.edu.upc.parkingnow.presentation.view

import pe.edu.upc.parkingnow.R
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel
import pe.edu.upc.parkingnow.presentation.viewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.upc.parkingnow.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, appViewModel: AppViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val isDarkTheme by appViewModel.isDarkMode.collectAsState()
    val scrollState = rememberScrollState()

    val loginViewModel: LoginViewModel = viewModel()
    val loginSuccess by loginViewModel.loginSuccess
    val loginError by loginViewModel.loginError

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("parkingnow_prefs", android.content.Context.MODE_PRIVATE)

    val backgroundColor = if (isDarkTheme)
        Color(0xFF121212)
    else
        Color(0xFFF5F9FF)

    val cardColor = if (isDarkTheme)
        Color(0xFF1E1E1E)
    else
        Color.White

    val textColor = if (isDarkTheme)
        Color.White
    else
        Color(0xFF1E293B)

    val secondaryTextColor = if (isDarkTheme)
        Color.LightGray
    else
        Color.Gray

    val accentColor = Color(0xFF4285F4)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Background image with overlay gradient (only in light mode)
        if (!isDarkTheme) {
            Image(
                painter = painterResource(id = R.drawable.login_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Semi-transparent overlay for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.7f),
                                Color.White.copy(alpha = 0.85f)
                            )
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section with enhanced branding
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 48.dp)
            ) {
                // Enhanced app logo with animation-ready design
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    accentColor,
                                    Color(0xFF1976D2)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalParking,
                        contentDescription = "ParkingNow Logo",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "ParkingNow",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )

                Text(
                    text = "Encuentra el mejor estacionamiento",
                    fontSize = 16.sp,
                    color = secondaryTextColor,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }

            // Enhanced login form
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Welcome header with icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(accentColor.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "¡Bienvenido de vuelta!",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Text(
                                text = "Inicia sesión en tu cuenta",
                                fontSize = 14.sp,
                                color = secondaryTextColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Enhanced email field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = accentColor
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (isDarkTheme) Color(0xFF2C2C2E) else Color.White,
                            unfocusedContainerColor = if (isDarkTheme) Color(0xFF2C2C2E) else Color.White,
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color(0xFFBDBDBD),
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            focusedLabelColor = accentColor,
                            unfocusedLabelColor = secondaryTextColor
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Enhanced password field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = accentColor
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = accentColor
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (isDarkTheme) Color(0xFF2C2C2E) else Color.White,
                            unfocusedContainerColor = if (isDarkTheme) Color(0xFF2C2C2E) else Color.White,
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color(0xFFBDBDBD),
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            focusedLabelColor = accentColor,
                            unfocusedLabelColor = secondaryTextColor
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Enhanced forgot password link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { navController.navigate(Routes.ForgotPassword.route) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Help,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "¿Olvidaste tu contraseña?",
                                fontSize = 14.sp,
                                color = accentColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Enhanced login button
                    val isFormValid = email.isNotBlank() && password.isNotBlank()

                    Button(
                        onClick = {
                            if (isFormValid) {
                                loginViewModel.login(pe.edu.upc.parkingnow.data.model.LoginRequest(email, password))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFormValid) accentColor else secondaryTextColor,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = if (isFormValid) 8.dp else 2.dp
                        ),
                        enabled = isFormValid
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Login,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Iniciar Sesión",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (loginSuccess != null && loginSuccess?.access_token?.isNotBlank() == true) {
                        // Guardar datos del usuario autenticado en SharedPreferences
                        LaunchedEffect(loginSuccess) {
                            loginSuccess?.let { resp ->
                                sharedPreferences.edit().apply {
                                    putString("user_token", resp.access_token)  // ← Cambiado de "access_token" a "user_token"
                                    putString("user_name", resp.user.name)
                                    putString("user_email", resp.user.email)
                                    putString("user_tipo", resp.user.tipoUsuario)
                                    putInt("user_id", resp.user.id)
                                    apply()
                                }
                            }
                            navController.navigate(Routes.Dashboard.route) {
                                popUpTo(Routes.Login.route) { inclusive = true }
                            }
                            navController.currentBackStackEntry?.savedStateHandle?.set("reset_terms", true)
                        }
                    }
                    if (loginError != null) {
                        Text(
                            text = "Error al iniciar sesión: ${loginError ?: "Inténtalo de nuevo"}",
                            color = Color.Red,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Enhanced divider
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = secondaryTextColor.copy(alpha = 0.3f)
                        )
                        Card(
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(
                                containerColor = cardColor
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Text(
                                text = "O",
                                color = secondaryTextColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                        Divider(
                            modifier = Modifier.weight(1f),
                            color = secondaryTextColor.copy(alpha = 0.3f)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Enhanced register button
                    OutlinedButton(
                        onClick = { navController.navigate(Routes.Register.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, accentColor),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = accentColor
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Crear una cuenta",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                }
            }

            // Enhanced footer with additional options
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Quick access buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = {
                            navController.navigate(Routes.Dashboard.route) {
                                popUpTo(Routes.Login.route) { inclusive = true }
                            }
                            navController.currentBackStackEntry?.savedStateHandle?.set("reset_terms", true)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = secondaryTextColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Modo invitado",
                            fontSize = 12.sp,
                            color = secondaryTextColor
                        )
                    }

                    TextButton(
                        onClick = { /* Help */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Help,
                            contentDescription = null,
                            tint = secondaryTextColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Ayuda",
                            fontSize = 12.sp,
                            color = secondaryTextColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Copyright with enhanced styling
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor.copy(alpha = 0.5f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = "© 2025 ParkingNow. Todos los derechos reservados.",
                        color = secondaryTextColor,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

