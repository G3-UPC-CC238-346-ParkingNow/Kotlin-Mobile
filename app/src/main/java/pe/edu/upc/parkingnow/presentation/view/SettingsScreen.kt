package pe.edu.upc.parkingnow.presentation.view

import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.CarCrash
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, appViewModel: AppViewModel) {
    val context = LocalContext.current
    val viewModel = appViewModel
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val darkModeEnabled by viewModel.isDarkMode.collectAsState()

    val backgroundColor = if (darkModeEnabled) Color(0xFF121212) else null
    val backgroundBrush = if (!darkModeEnabled) Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE6EEFF),
            Color(0xFFF5F9FF)
        )
    ) else null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush ?: SolidColor(backgroundColor ?: Color.White))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Top bar with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Regresar",
                        tint = if (darkModeEnabled) Color.White else Color(0xFF4285F4)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Configuración",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (darkModeEnabled) Color.White else Color(0xFF333333)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App icon and version
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF4285F4)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "P",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "ParkingNow",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (darkModeEnabled) Color.White else Color(0xFF333333)
                    )
                    Text(
                        text = "Versión 1.0.0",
                        fontSize = 14.sp,
                        color = if (darkModeEnabled) Color.Gray else Color.Gray
                    )
                }
            }

            // Main settings items
            SettingsCategory(title = "Cuenta y Seguridad", darkModeEnabled = darkModeEnabled)

            SettingsItem(
                icon = Icons.Default.Lock,
                title = "Privacidad y políticas",
                onClick = { /* Show toast or navigate */ },
                navController = navController,
                darkModeEnabled = darkModeEnabled
            )

            SettingsItem(
                icon = Icons.Default.MonetizationOn,
                title = "Pedir reembolso",
                onClick = { /* Show toast or navigate */ },
                navController = navController,
                darkModeEnabled = darkModeEnabled
            )

            SettingsItem(
                icon = Icons.Default.CarCrash,
                title = "Registrar robo de carro",
                onClick = { /* Show toast or navigate */ },
                navController = navController,
                tint = Color(0xFFE53935), // Red tint for important action
                darkModeEnabled = darkModeEnabled
            )

            SettingsItem(
                icon = Icons.Default.Delete,
                title = "Eliminar cuenta",
                onClick = { /* Show toast or navigate */ },
                navController = navController,
                tint = Color(0xFFE53935), // Red tint for dangerous action
                darkModeEnabled = darkModeEnabled
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Additional settings
            SettingsCategory(title = "Preferencias", darkModeEnabled = darkModeEnabled)

            // Notification toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = if (darkModeEnabled) Color.White else Color(0xFF4285F4),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Notificaciones",
                    fontSize = 16.sp,
                    color = if (darkModeEnabled) Color.White else Color(0xFF333333),
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { viewModel.toggleNotifications(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF4285F4),
                        checkedTrackColor = Color(0xFFBBDEFB)
                    )
                )
            }

            // Dark mode toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DarkMode,
                    contentDescription = null,
                    tint = if (darkModeEnabled) Color.White else Color(0xFF4285F4),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Modo oscuro",
                    fontSize = 16.sp,
                    color = if (darkModeEnabled) Color.White else Color(0xFF333333),
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = { viewModel.toggleDarkMode(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF4285F4),
                        checkedTrackColor = Color(0xFFBBDEFB)
                    )
                )
            }

            // Language selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = if (darkModeEnabled) Color.White else Color(0xFF4285F4),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Idioma",
                    fontSize = 16.sp,
                    color = if (darkModeEnabled) Color.White else Color(0xFF333333),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "Español",
                    fontSize = 16.sp,
                    color = if (darkModeEnabled) Color.Gray else Color.Gray
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(start = 4.dp)
                        .rotate(180f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // App version at bottom
            Text(
                text = "© 2023 ParkingNow. Todos los derechos reservados.",
                fontSize = 12.sp,
                color = if (darkModeEnabled) Color.Gray else Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun SettingsCategory(title: String, darkModeEnabled: Boolean) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = if (darkModeEnabled) Color.White else Color(0xFF4285F4),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    navController: NavController,
    tint: Color = Color(0xFF4285F4),
    darkModeEnabled: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (darkModeEnabled) Color(0xFF1E1E1E) else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                color = if (darkModeEnabled) Color.White else Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(180f)
            )
        }
    }
}

// Extension function to rotate composable
fun Modifier.rotate(degrees: Float): Modifier {
    return this.then(
        Modifier.graphicsLayer(
            rotationZ = degrees
        )
    )
}