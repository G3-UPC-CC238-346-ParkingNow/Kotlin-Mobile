package pe.edu.upc.parkingnow.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import pe.edu.upc.parkingnow.R
import kotlinx.coroutines.delay

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    var email by remember { mutableStateOf("") }
    var showConfirmation by remember { mutableStateOf(false) }
    var showEmptyWarning by remember { mutableStateOf(false) }
    var triggerNavigation by remember { mutableStateOf(false) }

    LaunchedEffect(triggerNavigation) {
        if (triggerNavigation) {
            delay(3000)
            showConfirmation = false
            triggerNavigation = false
            navController.navigate(pe.edu.upc.parkingnow.presentation.navigation.Routes.ChangePassword.route)
        }
    }
    LaunchedEffect(showEmptyWarning) {
        if (showEmptyWarning) {
            delay(3000)
            showEmptyWarning = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = { navController.navigate(pe.edu.upc.parkingnow.presentation.navigation.Routes.Login.route) },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 32.dp, start = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
        if (showConfirmation) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 24.dp, vertical = 72.dp),
                shape = RoundedCornerShape(12.dp),
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ) {
                Text(
                    text = "A password reset email has been sent.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        if (showEmptyWarning) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 24.dp, vertical = 72.dp),
                shape = RoundedCornerShape(12.dp),
                containerColor = Color(0xFFD32F2F),
                contentColor = Color.White
            ) {
                Text(
                    text = "Please enter your email before continuing.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 96.dp, bottom = 48.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 64.dp, bottom = 64.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Forgot\npassword",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0D0D0D)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Please enter your email to change your password",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
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
                        if (email.isNotBlank() && isValidEmail(email)) {
                            showEmptyWarning = false
                            showConfirmation = true
                            triggerNavigation = true
                        } else {
                            showEmptyWarning = true
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
                    Text("Continue", fontSize = 16.sp, color = Color.Black)
                }
            }
        }
    }
}