package pe.edu.upc.parkingnow.presentation.view

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upc.parkingnow.presentation.viewmodel.UserViewModel
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel
import pe.edu.upc.parkingnow.R
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    userViewModel: UserViewModel,
    appViewModel: AppViewModel
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val isDarkTheme by appViewModel.isDarkMode.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image with overlay gradient
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        // Semi-transparent overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (isDarkTheme) {
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF121212), Color(0xFF121212))
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.7f),
                                Color.White.copy(alpha = 0.85f)
                            )
                        )
                    }
                )
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar with back button and title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                // Back button
                IconButton(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Title
                Text(
                    text = "Crear Cuenta",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Registration card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header
                    Text(
                        text = "Información Personal",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                    )

                    // Name field
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre completo") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            unfocusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            focusedBorderColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color(0xFFBDBDBD),
                            focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            focusedLabelColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedLabelColor = if (isDarkTheme) Color.LightGray else Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    // Email field
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            unfocusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            focusedBorderColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color(0xFFBDBDBD),
                            focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            focusedLabelColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedLabelColor = if (isDarkTheme) Color.LightGray else Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    Divider(color = if (isDarkTheme) Color.LightGray.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f))

                    // Security section header
                    Text(
                        text = "Seguridad",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                    )

                    // Password field
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            unfocusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            focusedBorderColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color(0xFFBDBDBD),
                            focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            focusedLabelColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedLabelColor = if (isDarkTheme) Color.LightGray else Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    // Confirm password field
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar contraseña") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    if (confirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (confirmPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                    tint = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            unfocusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            focusedBorderColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color(0xFFBDBDBD),
                            focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            focusedLabelColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedLabelColor = if (isDarkTheme) Color.LightGray else Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    Divider(color = if (isDarkTheme) Color.LightGray.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f))

                    // Vehicle information section header
                    Text(
                        text = "Información del Vehículo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                    )

                    // Vehicle plate field
                    OutlinedTextField(
                        value = plate,
                        onValueChange = { plate = it },
                        label = { Text("Placa del vehículo") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.DirectionsCar,
                                contentDescription = null,
                                tint = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            unfocusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            focusedBorderColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color(0xFFBDBDBD),
                            focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            focusedLabelColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedLabelColor = if (isDarkTheme) Color.LightGray else Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    // DNI field
                    OutlinedTextField(
                        value = dni,
                        onValueChange = { dni = it },
                        label = { Text("Documento de identidad (DNI)") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Badge,
                                contentDescription = null,
                                tint = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            unfocusedContainerColor = if (isDarkTheme) Color(0xFF23272F) else Color.White,
                            focusedBorderColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedBorderColor = if (isDarkTheme) Color.LightGray else Color(0xFFBDBDBD),
                            focusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            unfocusedTextColor = if (isDarkTheme) Color.White else Color.Black,
                            focusedLabelColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            unfocusedLabelColor = if (isDarkTheme) Color.LightGray else Color.Gray
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Terms and conditions text
            Text(
                text = "Al registrarte, aceptas nuestros Términos y Condiciones y nuestra Política de Privacidad",
                fontSize = 12.sp,
                color = if (isDarkTheme) Color.LightGray else Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register button
            Button(
                onClick = {
                    if (name.isNotBlank() &&
                        email.isNotBlank() &&
                        password.isNotBlank() &&
                        confirmPassword.isNotBlank() &&
                        plate.isNotBlank() &&
                        dni.isNotBlank() &&
                        password == confirmPassword
                    ) {
                        userViewModel.setUsername(name)
                        navController.navigate("dashboard") {
                            // Clear the back stack so user can't go back to register
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkTheme) Color(0xFFE0E0E0) else Color(0xFF4285F4),
                    contentColor = if (isDarkTheme) Color.Black else Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Text(
                    text = "Crear Cuenta",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Already have account button
            TextButton(
                onClick = { navController.navigate("login") }
            ) {
                Text(
                    text = "¿Ya tienes una cuenta? Inicia sesión",
                    fontSize = 14.sp,
                    color = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}