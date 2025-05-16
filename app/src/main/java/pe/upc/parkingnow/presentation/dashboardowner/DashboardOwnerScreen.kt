package pe.upc.parkingnow.presentation.dashboardowner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pe.upc.parkingnow.R

@Composable
fun DashboardOwnerScreen(
    ownerName: String = "John Smith",
    onLogout: () -> Unit = {},
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenuContent(
                ownerName = ownerName,
                onMenuSelected = { /* Aquí puedes navegar según la opción */ },
                onLogout = onLogout,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        // Dashboard principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F6FA))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { scope.launch { drawerState.open() } },
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color(0xFF232943)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    // Imagen de dueño
                    Image(
                        painter = painterResource(R.drawable.duenho),
                        contentDescription = "Foto Dueño",
                        modifier = Modifier
                            .size(58.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Dashboard Dueño de estacionamiento",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF232943),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Reservas para hoy",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color(0xFF232943)
                )
                Spacer(modifier = Modifier.height(7.dp))

                Button(
                    onClick = { /* Acción reservas programadas */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF232943)
                    ),
                    elevation = ButtonDefaults.buttonElevation(1.dp)
                ) {
                    Text("Reservas programadas", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Espacios:",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = Color(0xFF232943)
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Lista de espacios separados por:") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(16.dp))
                )
            }
        }
    }
}

@Composable
private fun DrawerMenuContent(
    ownerName: String,
    onMenuSelected: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    closeDrawer: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(Color(0xFFF5F6FA))
            .padding(horizontal = 18.dp, vertical = 24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.duenho),
                contentDescription = "Foto Dueño",
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = ownerName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF232943)
                )
                Text(
                    text = "Lima, Perú",
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = Color(0xFF838A9C)
                )
            }
        }
        Spacer(modifier = Modifier.height(28.dp))

        DrawerMenuItem("Inicio", closeDrawer)
        DrawerMenuItem("Registro de locales", closeDrawer)
        DrawerMenuItem("Reservas", closeDrawer)
        DrawerMenuItem("Seguridad", closeDrawer)
        DrawerMenuItem("Configuración", closeDrawer)
        DrawerMenuItem("Notificación", closeDrawer)
        Spacer(modifier = Modifier.weight(1f))

        // Logout
        TextButton(
            onClick = { onLogout(); closeDrawer() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color(0xFFF94B4B))
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                "Logout",
                color = Color(0xFFF94B4B),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun DrawerMenuItem(
    title: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            color = Color(0xFF232943),
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            modifier = Modifier.padding(vertical = 3.dp)
        )
    }
}