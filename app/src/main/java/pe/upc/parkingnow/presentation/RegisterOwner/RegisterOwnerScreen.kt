package pe.upc.parkingnow.presentation.login.RegisterOwner

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterOwnerScreen(
    onBack: () -> Unit = {},
    onRegister: (RegisterOwnerState) -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {
    var state by remember { mutableStateOf(RegisterOwnerState()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
    ) {
        // Círculo decorativo arriba a la derecha
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
                text = "Register Dueño de estacionamiento",
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
                label = "RUC",
                value = state.ruc,
                onValueChange = { state = state.copy(ruc = it) },
                placeholder = "Ingrese RUC de su estacionamiento"
            )
            RegisterTextField(
                label = "Registro del local",
                value = state.businessName,
                onValueChange = { state = state.copy(businessName = it) },
                placeholder = "Ingrese el nombre del estacionamiento"
            )
            RegisterTextField(
                label = "Dirección del estacionamiento",
                value = state.address,
                onValueChange = { state = state.copy(address = it) },
                placeholder = "Ingrese dirección"
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

// --- Utilidades reutilizables ---

@Composable
fun RegisterTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
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
        leadingIcon = leadingIcon?.let {
            { Icon(it, contentDescription = null, tint = Color(0xFF5468FF)) }
        },
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(11.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(bottom = 2.dp)
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
        leadingIcon = {
            Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = Color(0xFF5468FF))
        },
        trailingIcon = {
            IconButton(onClick = { onVisibilityChange() }) {
                Icon(
                    imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (visible) "Ocultar" else "Mostrar"
                )
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(11.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(bottom = 2.dp),
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation()
    )
}