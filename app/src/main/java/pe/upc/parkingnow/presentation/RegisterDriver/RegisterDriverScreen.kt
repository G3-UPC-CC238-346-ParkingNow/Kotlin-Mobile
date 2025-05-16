package pe.upc.parkingnow.presentation.login.RegisterDriver

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterDriverScreen(
    onBack: () -> Unit = {},
    onRegister: (RegisterDriverState) -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {
    var state by remember { mutableStateOf(RegisterDriverState()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
    ) {
        // Círculo decorativo
        Box(
            modifier = Modifier
                .size(90.dp)
                .offset(x = 170.dp, y = 0.dp)
                .clip(CircleShape)
                .background(Color(0xFF6B7AFF))
                .align(Alignment.TopEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            IconButton(onClick = { onBack() }, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF232943))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Register Conductor",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color(0xFF232943),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(7.dp))
            RegisterTextField(
                label = "Your Name",
                value = state.name,
                onValueChange = { state = state.copy(name = it) },
                leadingIcon = Icons.Default.Person,
                placeholder = "John Smith"
            )
            RegisterTextField(
                label = "Email",
                value = state.email,
                onValueChange = { state = state.copy(email = it) },
                leadingIcon = Icons.Default.Email,
                placeholder = "johnsmith@domain.abc"
            )
            RegisterPasswordField(
                label = "Password",
                value = state.password,
                onValueChange = { state = state.copy(password = it) },
                visible = state.isPasswordVisible,
                onVisibilityChange = { state = state.copy(isPasswordVisible = !state.isPasswordVisible) }
            )
            RegisterPasswordField(
                label = "Confirmación Password",
                value = state.confirmPassword,
                onValueChange = { state = state.copy(confirmPassword = it) },
                visible = state.isConfirmPasswordVisible,
                onVisibilityChange = { state = state.copy(isConfirmPasswordVisible = !state.isConfirmPasswordVisible) }
            )
            RegisterTextField(
                label = "Registrar placa de vehículo",
                value = state.licensePlate,
                onValueChange = { state = state.copy(licensePlate = it) },
                placeholder = "Ingrese la placa de su carro"
            )
            RegisterTextField(
                label = "DNI",
                value = state.dni,
                onValueChange = { state = state.copy(dni = it) },
                placeholder = "Ingrese su DNI"
            )
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onRegister(state)
                    onRegisterSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 22.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF232943),
                    contentColor = Color.White
                )
            ) {
                Text("Registrarse", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }
        }
    }
}

@Composable
fun RegisterTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector? = null,
    placeholder: String = ""
) {
    Text(
        text = label,
        color = Color(0xFF232943),
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = if (leadingIcon != null) {
            { Icon(leadingIcon, contentDescription = null, tint = Color(0xFF5468FF)) }
        } else null,
        placeholder = { Text(placeholder, color = Color(0xFFB3B9C9)) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun RegisterPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onVisibilityChange: () -> Unit
) {
    Text(
        text = label,
        color = Color(0xFF232943),
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        modifier = Modifier.padding(top = 8.dp, bottom = 3.dp)
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("********", color = Color(0xFFB3B9C9)) },
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onVisibilityChange) {
                Icon(
                    if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (visible) "Ocultar" else "Mostrar"
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(12.dp)
    )
}