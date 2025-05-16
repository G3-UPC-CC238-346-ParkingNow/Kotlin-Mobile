package pe.upc.parkingnow.presentation.SelectRole

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.upc.parkingnow.R

@Composable
fun SelectRoleScreen(
    onRoleSelected: (Role) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F6FA))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Select to Role",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color(0xFF232943)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Card principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(22.dp))
                .padding(vertical = 24.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Opción Conductor
            RoleCard(
                imageRes = R.drawable.conductor,
                description = "Regístrese como conductor para tener al alcance información de todos los estacionamientos en toda la ciudad",
                onClick = { onRoleSelected(Role.DRIVER) }
            )
            Spacer(modifier = Modifier.height(20.dp))
            // Opción Dueño/Empresa
            RoleCard(
                imageRes = R.drawable.duenho,
                description = "Regístrese como Empresa para poder facilitar la gestión de los espacios que brinda y acelerar los pagos",
                onClick = { onRoleSelected(Role.OWNER) }
            )
        }
    }
}

// Card de cada rol
@Composable
fun RoleCard(
    imageRes: Int,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(78.dp)
                .padding(6.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = description,
            fontSize = 15.sp,
            color = Color(0xFF676D7C),
            fontWeight = FontWeight.Normal,
            lineHeight = 21.sp
        )
    }
}

// Enum para roles
enum class Role {
    DRIVER, OWNER
}