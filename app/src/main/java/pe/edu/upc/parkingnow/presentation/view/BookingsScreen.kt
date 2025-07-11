package pe.edu.upc.parkingnow.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import pe.edu.upc.parkingnow.presentation.navigation.Routes
import pe.edu.upc.parkingnow.R
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel
import pe.edu.upc.parkingnow.presentation.viewmodel.ReservationViewModel
import pe.edu.upc.parkingnow.data.model.ReservaActivaResponse
import android.widget.Toast
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.util.Locale
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingsScreen(navController: NavController, appViewModel: AppViewModel, reservationViewModel: ReservationViewModel) {
    val isDarkTheme by appViewModel.isDarkMode.collectAsState()
    val selectedItem = remember { mutableStateOf("bookings") }
    val scrollState = rememberScrollState()

    // Estados para el modal de reservas activas
    var showReservasActivasModal by remember { mutableStateOf(false) }
    val reservasActivas by reservationViewModel.reservasActivas.collectAsState()
    val isLoadingReservas by reservationViewModel.isLoadingReservas.collectAsState()
    val reservasError by reservationViewModel.reservasError.collectAsState()

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("parkingnow_prefs", android.content.Context.MODE_PRIVATE)
    val userToken = sharedPreferences.getString("user_token", "") ?: ""

    // Mostrar errores si los hay
    LaunchedEffect(reservasError) {
        reservasError?.let { error ->
            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            reservationViewModel.clearReservasStates()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Semi-transparent overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = if (isDarkTheme) {
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF121212),
                                Color(0xFF121212)
                            )
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Top bar
            TopAppBar(
                title = {
                    Text(
                        text = "Mis Reservas",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.Dashboard.route) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isDarkTheme) Color.White else Color(0xFF4285F4)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Icon",
                            tint = if (isDarkTheme) Color(0xFF1E293B) else Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 100.dp), // Space for bottom navigation
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Active reservations section
                EnhancedBookingCard(
                    title = "Reservas Activas",
                    subtitle = "Gestiona tus reservas actuales",
                    icon = Icons.Default.CalendarToday,
                    backgroundColor = if (isDarkTheme) Color(0xFF232D23) else Color(0xFFE8F5E8),
                    iconColor = if (isDarkTheme) Color(0xFF81C784) else Color(0xFF4CAF50),
                    buttonText = "Ver reservas activas",
                    onButtonClick = {
                        if (userToken.isNotEmpty()) {
                            reservationViewModel.obtenerReservasActivas(userToken)
                            showReservasActivasModal = true
                        } else {
                            Toast.makeText(context, "No hay sesión activa", Toast.LENGTH_SHORT).show()
                        }
                    },
                    isDarkTheme = isDarkTheme
                )

                // Make new reservation section
                EnhancedBookingCard(
                    title = "Nueva Reserva",
                    subtitle = "Encuentra y reserva un estacionamiento",
                    icon = Icons.Default.Add,
                    backgroundColor = if (isDarkTheme) Color(0xFF233045) else Color(0xFFE3F2FD),
                    iconColor = if (isDarkTheme) Color(0xFF90CAF9) else Color(0xFF2196F3),
                    buttonText = "Realizar reserva",
                    onButtonClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("parkingName", "Real Plaza Salaverry")
                        navController.currentBackStackEntry?.savedStateHandle?.set("parkingPrice", "4.00")
                        navController.navigate("ticket")
                    },
                    isDarkTheme = isDarkTheme
                )

                // History section
                EnhancedBookingCard(
                    title = "Historial",
                    subtitle = "Revisa tus reservas anteriores",
                    icon = Icons.Default.History,
                    backgroundColor = if (isDarkTheme) Color(0xFF33291F) else Color(0xFFFFF3E0),
                    iconColor = if (isDarkTheme) Color(0xFFFFB74D) else Color(0xFFFF9800),
                    buttonText = "Ver historial",
                    onButtonClick = { /* Navigate to history */ },
                    isDarkTheme = isDarkTheme
                )

                // Favorites section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDarkTheme) Color(0xFF2D1A1C) else Color(0xFFFFEBEE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = if (isDarkTheme) Color(0xFFFF80AB) else Color(0xFFE91E63),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Lugares Favoritos",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                                )
                                Text(
                                    text = "Tus estacionamientos preferidos",
                                    fontSize = 14.sp,
                                    color = if (isDarkTheme) Color.Gray else Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Favorite location item
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDarkTheme) Color(0xFF232323) else Color(0xFFF8F9FA)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Estacionamiento Real Plaza Salaverry",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (isDarkTheme) Color(0xFFFFC107) else Color(0xFFFFC107),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Saved places section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isDarkTheme) Color(0xFF2B2030) else Color(0xFFF3E5F5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = null,
                                    tint = if (isDarkTheme) Color(0xFFE1BEE7) else Color(0xFF9C27B0),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Lugares Guardados",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                                )
                                Text(
                                    text = "Estacionamientos que has guardado",
                                    fontSize = 14.sp,
                                    color = if (isDarkTheme) Color.Gray else Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Saved location item
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDarkTheme) Color(0xFF232323) else Color(0xFFF8F9FA)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Estacionamiento Kennedy Park - Miraflores",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = null,
                                    tint = if (isDarkTheme) Color(0xFFE1BEE7) else Color(0xFF9C27B0),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom navigation
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    NavigationBarItem(
                        selected = selectedItem.value == "home",
                        onClick = {
                            selectedItem.value = "home"
                            navController.navigate(Routes.Dashboard.route)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Home,
                                contentDescription = "Inicio",
                                tint = if (selectedItem.value == "home") {
                                    if (isDarkTheme) Color.White else Color(0xFF4285F4)
                                } else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                "Inicio",
                                color = if (selectedItem.value == "home") {
                                    if (isDarkTheme) Color.White else Color(0xFF4285F4)
                                } else Color.Gray,
                                fontWeight = if (selectedItem.value == "home") FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        selected = selectedItem.value == "bookings",
                        onClick = {
                            selectedItem.value = "bookings"
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Reservas",
                                tint = if (selectedItem.value == "bookings") {
                                    if (isDarkTheme) Color.White else Color(0xFF4285F4)
                                } else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                "Reservas",
                                color = if (selectedItem.value == "bookings") {
                                    if (isDarkTheme) Color.White else Color(0xFF4285F4)
                                } else Color.Gray,
                                fontWeight = if (selectedItem.value == "bookings") FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                    NavigationBarItem(
                        selected = selectedItem.value == "settings",
                        onClick = {
                            selectedItem.value = "settings"
                            navController.navigate("settings")
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Ajustes",
                                tint = if (selectedItem.value == "settings") {
                                    if (isDarkTheme) Color.White else Color(0xFF4285F4)
                                } else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                "Ajustes",
                                color = if (selectedItem.value == "settings") {
                                    if (isDarkTheme) Color.White else Color(0xFF4285F4)
                                } else Color.Gray,
                                fontWeight = if (selectedItem.value == "settings") FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                    )
                }
            }
        }
    }

    // Modal de Reservas Activas
    if (showReservasActivasModal) {
        ReservasActivasModal(
            reservas = reservasActivas,
            isLoading = isLoadingReservas,
            isDarkTheme = isDarkTheme,
            onDismiss = { showReservasActivasModal = false }
        )
    }
}

@Composable
fun ReservasActivasModal(
    reservas: List<ReservaActivaResponse>,
    isLoading: Boolean,
    isDarkTheme: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reservas Activas",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                        )
                    }
                }

                Divider(
                    color = if (isDarkTheme) Color.Gray.copy(alpha = 0.3f) else Color.LightGray.copy(alpha = 0.5f),
                    thickness = 1.dp
                )

                // Content
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF4285F4),
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Cargando reservas...",
                                fontSize = 16.sp,
                                color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                            )
                        }
                    }
                } else if (reservas.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = if (isDarkTheme) Color(0xFF90CAF9) else Color(0xFF4285F4),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No tienes reservas activas",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Cuando realices una reserva aparecerá aquí",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(reservas) { reserva ->
                            ReservaActivaCard(
                                reserva = reserva,
                                isDarkTheme = isDarkTheme
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReservaActivaCard(
    reserva: ReservaActivaResponse,
    isDarkTheme: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color(0xFF2C2C2C) else Color(0xFFF8F9FA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con código y estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Código: ${reserva.codigoReserva}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4285F4)
                )

                ReservationStatusChip(
                    status = reserva.estado,
                    isDarkTheme = isDarkTheme
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre del local
            Text(
                text = reserva.nombreLocal,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
            )

            // Dirección
            Text(
                text = reserva.direccionLocal,
                fontSize = 12.sp,
                color = if (isDarkTheme) Color.LightGray else Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fechas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Inicio",
                        fontSize = 10.sp,
                        color = if (isDarkTheme) Color.LightGray else Color.Gray
                    )
                    Text(
                        text = formatDateTime(reserva.fechaInicio),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Fin",
                        fontSize = 10.sp,
                        color = if (isDarkTheme) Color.LightGray else Color.Gray
                    )
                    Text(
                        text = formatDateTime(reserva.fechaFin),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Información del vehículo y precio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${reserva.tipoVehiculo.uppercase()} - ${reserva.placaVehiculo}",
                    fontSize = 12.sp,
                    color = if (isDarkTheme) Color.LightGray else Color.Gray
                )

                Text(
                    text = "S/ ${reserva.precioTotal}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun ReservationStatusChip(
    status: String,
    isDarkTheme: Boolean
) {
    val (backgroundColor, textColor, text) = when (status.lowercase()) {
        "pendiente" -> Triple(
            Color(0xFFFF9800).copy(alpha = 0.1f),
            Color(0xFFFF9800),
            "Pendiente"
        )
        "confirmada" -> Triple(
            Color(0xFF2196F3).copy(alpha = 0.1f),
            Color(0xFF2196F3),
            "Confirmada"
        )
        "en_curso" -> Triple(
            Color(0xFF4CAF50).copy(alpha = 0.1f),
            Color(0xFF4CAF50),
            "En Curso"
        )
        "finalizada" -> Triple(
            Color(0xFF9E9E9E).copy(alpha = 0.1f),
            Color(0xFF9E9E9E),
            "Finalizada"
        )
        "cancelada" -> Triple(
            Color(0xFFF44336).copy(alpha = 0.1f),
            Color(0xFFF44336),
            "Cancelada"
        )
        else -> Triple(
            Color(0xFF9E9E9E).copy(alpha = 0.1f),
            Color(0xFF9E9E9E),
            status.replaceFirstChar { it.uppercase() }
        )
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

// Función para formatear las fechas con manejo seguro
fun formatDateTime(dateTimeString: String): String {
    return try {
        if (dateTimeString.isBlank()) return "Fecha no disponible"

        // Manejar diferentes formatos de fecha que puede retornar el backend
        val inputFormats = listOf(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        )

        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        for (inputFormat in inputFormats) {
            try {
                val date = inputFormat.parse(dateTimeString)
                date?.let { return outputFormat.format(it) }
            } catch (e: Exception) {
                // Continuar con el siguiente formato
                continue
            }
        }

        // Si ningún formato funcionó, hacer parsing manual
        if (dateTimeString.contains("T")) {
            val parts = dateTimeString.split("T")
            if (parts.size >= 2) {
                val datePart = parts[0].split("-")
                val timePart = parts[1].split(":")

                if (datePart.size >= 3 && timePart.size >= 2) {
                    return "${datePart[2]}/${datePart[1]}/${datePart[0]} ${timePart[0]}:${timePart[1]}"
                }
            }
        }

        // Si todo falla, retornar el string original
        dateTimeString
    } catch (e: Exception) {
        Log.e("DateFormatter", "Error formateando fecha: $dateTimeString", e)
        "Fecha no válida"
    }
}

@Composable
fun EnhancedBookingCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    buttonText: String,
    onButtonClick: () -> Unit,
    isDarkTheme: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                    )
                    Text(
                        text = subtitle,
                        fontSize = 14.sp,
                        color = if (isDarkTheme) Color.Gray else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onButtonClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkTheme) Color(0xFF4285F4) else Color(0xFF4285F4)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Text(
                    text = buttonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

