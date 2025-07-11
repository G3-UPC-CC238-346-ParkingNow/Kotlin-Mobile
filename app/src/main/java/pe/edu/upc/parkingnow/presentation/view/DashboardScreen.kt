package pe.edu.upc.parkingnow.presentation.view

import  pe.edu.upc.parkingnow.presentation.navigation.Routes

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.Geocoder
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.animation.AnimatedContent
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import pe.edu.upc.parkingnow.R
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.vector.ImageVector
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import java.util.Locale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.lazy.LazyRow

import pe.edu.upc.parkingnow.presentation.viewmodel.UserViewModel
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel
import androidx.activity.compose.BackHandler
import pe.edu.upc.parkingnow.data.repository.LocalRepositoryImpl
import pe.edu.upc.parkingnow.presentation.viewmodel.ReservationViewModel
import pe.edu.upc.parkingnow.domain.model.Local
import kotlin.compareTo
import kotlin.text.toInt
import kotlin.times

data class ParkingSpot(
    val id: String,
    val name: String,
    val address: String,
    val distance: String,
    val price: String,
    val rating: Float,
    val features: List<String>,
    val availableSpots: Int,
    val totalSpots: Int,
    val isOpen24h: Boolean,
    val latitude: Double,
    val longitude: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, userViewModel: UserViewModel, appViewModel: AppViewModel, reservationViewModel: ReservationViewModel) {
    val currentUsername by userViewModel.username.collectAsState()
    val isDarkTheme = appViewModel.isDarkMode.collectAsState().value
    val context = LocalContext.current
    Configuration.getInstance().load(context.applicationContext, context.getSharedPreferences("osmdroid", 0))
    val sharedPreferences = context.getSharedPreferences("parkingnow_prefs", android.content.Context.MODE_PRIVATE)
    // Bloque para mostrar los términos una sola vez tras login, registro o modo invitado
    val resetTerms = navController.currentBackStackEntry?.savedStateHandle?.get<Boolean>("reset_terms") ?: false
    if (resetTerms) {
        sharedPreferences.edit().remove("accepted_terms").apply()
        navController.currentBackStackEntry?.savedStateHandle?.set("reset_terms", false)
    }
    val hasAcceptedTerms = sharedPreferences.getBoolean("accepted_terms", false)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showTermsDialog by remember { mutableStateOf(!hasAcceptedTerms) }

    // Leer usuario autenticado de SharedPreferences
    val userName = remember { mutableStateOf("") }
    val userEmail = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        val name = sharedPreferences.getString("user_name", "") ?: ""
        val email = sharedPreferences.getString("user_email", "") ?: ""
        val token = sharedPreferences.getString("user_token", "") ?: ""
        val placa = sharedPreferences.getString("user_placa", "") ?: ""

        userName.value = name
        userEmail.value = email

        // Cargar datos del usuario en el ViewModel
        userViewModel.loadUserData(token, name, email, placa)
    }

    // Search and filter states
    var searchQuery by remember { mutableStateOf("") }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Todos") }
    var showReservationDialog by remember { mutableStateOf(false) }
    var selectedParking by remember { mutableStateOf<ParkingSpot?>(null) }

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val district = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }

    // Bloquea el botón físico o gesto de retroceso en esta pantalla
    BackHandler(enabled = true) {
        // No hacer nada para bloquear el retroceso
    }

    var userLat by remember { mutableStateOf<Double?>(null) }
    var userLng by remember { mutableStateOf<Double?>(null) }
    var parkingSpots by remember { mutableStateOf<List<ParkingSpot>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val reservationSuccess by reservationViewModel.reservationSuccess.collectAsState()
    val reservationError by reservationViewModel.reservationError.collectAsState()

    // Obtener ubicación exacta del usuario y cargar locales cercanos
    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                val location = fusedLocationClient.lastLocation.await()
                val lat = location?.latitude ?: -12.104544984423494
                val lng = location?.longitude ?: -76.96466964907471
                userLat = lat
                userLng = lng
                val localRepository = LocalRepositoryImpl()
                val locales = localRepository.getLocalesCercanos(lat, lng, 10000)
                parkingSpots = locales.map { local ->
                    val estLat = local.latitud?.toDoubleOrNull() ?: 0.0
                    val estLng = local.longitud?.toDoubleOrNull() ?: 0.0
                    val distance = if (userLat != null && userLng != null && estLat != 0.0 && estLng != 0.0) {
                        val results = FloatArray(1)
                        Location.distanceBetween(userLat!!, userLng!!, estLat, estLng, results)
                        val km = results[0] / 1000.0
                        String.format("%.2f km", km)
                    } else "-"
                    ParkingSpot(
                        id = local.id.toString(),
                        name = local.nombre,
                        address = local.direccion,
                        distance = distance,
                        price = local.precio_por_hora?.let { "S/ $it/hora" } ?: "-",
                        rating = local.rating?.toFloatOrNull() ?: 0.0f,
                        features = buildList {
                            local.caracteristicas?.let { addAll(it) }
                            if (local.tiene_camaras == true) add("Cámaras de Seguridad")
                            if (local.seguridad_24h == true) add("Seguridad 24h")
                            if (local.techado == true) add("Techado")
                        },
                        availableSpots = local.espacios_disponibles ?: 0,
                        totalSpots = local.plazas,
                        isOpen24h = local.seguridad_24h == true,
                        latitude = estLat,
                        longitude = estLng
                    )
                }
            } catch (e: Exception) {
                // Si hay error, usar ubicación fija por defecto
                userLat = -12.104544984423494
                userLng = -76.96466964907471
                val localRepository = LocalRepositoryImpl()
                val locales = localRepository.getLocalesCercanos(userLat!!, userLng!!, 10000)
                parkingSpots = locales.map { local ->
                    val estLat = local.latitud?.toDoubleOrNull() ?: 0.0
                    val estLng = local.longitud?.toDoubleOrNull() ?: 0.0
                    val distance = if (userLat != null && userLng != null && estLat != 0.0 && estLng != 0.0) {
                        val results = FloatArray(1)
                        Location.distanceBetween(userLat!!, userLng!!, estLat, estLng, results)
                        val km = results[0] / 1000.0
                        String.format("%.2f km", km)
                    } else "-"
                    ParkingSpot(
                        id = local.id.toString(),
                        name = local.nombre,
                        address = local.direccion,
                        distance = distance,
                        price = local.precio_por_hora?.let { "S/ $it/hora" } ?: "-",
                        rating = local.rating?.toFloatOrNull() ?: 0.0f,
                        features = buildList {
                            local.caracteristicas?.let { addAll(it) }
                            if (local.tiene_camaras == true) add("Cámaras de Seguridad")
                            if (local.seguridad_24h == true) add("Seguridad 24h")
                            if (local.techado == true) add("Techado")
                        },
                        availableSpots = local.espacios_disponibles ?: 0,
                        totalSpots = local.plazas,
                        isOpen24h = local.seguridad_24h == true,
                        latitude = estLat,
                        longitude = estLng
                    )
                }
                errorMsg = "No se pudo obtener la ubicación, usando ubicación por defecto."
            }
            isLoading = false
        }
    }

    val filteredParkingSpots = remember(searchQuery, selectedFilter, parkingSpots) {
        var spots = parkingSpots.filter { spot ->
            val matchesSearch = spot.name.contains(searchQuery, ignoreCase = true) ||
                    spot.address.contains(searchQuery, ignoreCase = true)
            matchesSearch
        }
        when (selectedFilter) {
            "Más cercano" -> spots.sortedBy { spot ->
                spot.distance.replace(" km", "").toDoubleOrNull() ?: Double.MAX_VALUE
            }
            "Más barato" -> spots.sortedBy { spot ->
                spot.price.replace("S/ ", "").replace("/hora", "").replace("/h", "").toDoubleOrNull() ?: Double.MAX_VALUE
            }
            "Mejor valorado" -> spots.filter { it.rating >= 4.5f }
            "Disponible 24h" -> spots.filter { it.isOpen24h }
            else -> spots
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(reservationSuccess, reservationError) {
        reservationSuccess?.let { response ->
            Toast.makeText(context, "Reserva creada exitosamente: ${response.codigo_reserva}", Toast.LENGTH_LONG).show()
        }
        reservationError?.let { error ->
            Toast.makeText(context, "Error al crear la reserva: $error", Toast.LENGTH_LONG).show()
        }
    }

    // Background gradient
    val backgroundGradient = if (isDarkTheme) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF121212),
                Color(0xFF121212)
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE6EEFF),
                Color(0xFFF5F9FF),
                Color.White
            )
        )
    }

    // Terms Dialog mejorado
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(
                    onClick = {
                        sharedPreferences.edit().putBoolean("accepted_terms", true).apply()
                        showTermsDialog = false
                        Toast.makeText(context, "Gracias por aceptar los términos", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1D4ED8))
                ) {
                    Text("Acepto", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    Toast.makeText(context, "Debes aceptar los términos para usar la app", Toast.LENGTH_LONG).show()
                }) {
                    Text("Salir", color = Color(0xFF1D4ED8))
                }
            },
            title = { Text("Términos y Condiciones", color = Color.Black, style = MaterialTheme.typography.titleLarge) },
            text = {
                Text(
                    "Para continuar utilizando ParkingNow, debes aceptar los Términos y Condiciones. Estos establecen las normas de uso de la plataforma, el manejo de tus datos y tu compromiso con la comunidad.",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(12.dp)
        )
    }

    // Search Dialog
    if (showSearchDialog) {
        Dialog(onDismissRequest = { showSearchDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Buscar Estacionamiento",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("¿Dónde quieres estacionar?") },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4285F4),
                            focusedLabelColor = Color(0xFF4285F4)
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                showSearchDialog = false
                                Toast.makeText(context, "Búsqueda cancelada", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF4285F4)
                            )
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                showSearchDialog = false
                                Toast.makeText(context, "Buscando: $searchQuery", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4285F4)
                            )
                        ) {
                            Text("Buscar", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // Filter Dialog
    if (showFilterDialog) {
        Dialog(onDismissRequest = { showFilterDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Filtrar Resultados",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    val filterOptions = listOf("Todos", "Más cercano", "Más barato", "Mejor valorado", "Disponible 24h")

                    filterOptions.forEach { filter ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedFilter = filter }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF4285F4)
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = filter,
                                fontSize = 16.sp,
                                color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            showFilterDialog = false
                            Toast.makeText(context, "Filtro aplicado: $selectedFilter", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4285F4)
                        )
                    ) {
                        Text("Aplicar Filtro", color = Color.White)
                    }
                }
            }
        }
    }

    // Reservation Dialog
    if (showReservationDialog && selectedParking != null) {
        // Collect states outside the lambda to avoid @Composable context issues
        val userToken by userViewModel.userToken.collectAsState()
        val userPlaca by userViewModel.userPlaca.collectAsState()

        ReservationDialog(
            parking = selectedParking!!,
            isDarkTheme = isDarkTheme,
            onDismiss = { showReservationDialog = false },
            onConfirm = { duration, vehicleType, startDateTime, placa ->
                showReservationDialog = false
                reservationViewModel.crearReserva(
                    token = userToken,
                    idLocal = selectedParking!!.id.toInt(),
                    fhInicio = startDateTime,
                    duracionHoras = duration.toDouble(),
                    tipoVehiculo = vehicleType,
                    placaVehiculo = placa
                )
                navController.navigate(Routes.Payment.route)
            }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
            ) {
                // Header with gradient background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            brush = if (isDarkTheme) {
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF2C2C2C),
                                        Color(0xFF2C2C2C)
                                    )
                                )
                            } else {
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF4285F4),
                                        Color(0xFF1976D2)
                                    )
                                )
                            }
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .border(2.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userName.value.take(1).uppercase(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Hola,",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = userName.value,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            AnimatedContent(
                                targetState = "${district.value}, ${city.value}".trim(',', ' ')
                            ) { locationText ->
                                Text(
                                    text = locationText,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Menu items with better styling
                DrawerMenuItem("Inicio", Icons.Outlined.Home, true, isDarkTheme) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Dashboard.route)
                }
                DrawerMenuItem("Reservas", Icons.Outlined.CalendarToday, false, isDarkTheme) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Bookings.route)
                }
                DrawerMenuItem("Soporte", Icons.Outlined.SupportAgent, false, isDarkTheme) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Support.route)
                }
                DrawerMenuItem("Seguimiento", Icons.Outlined.Place, false, isDarkTheme) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Tracking.route)
                }
                DrawerMenuItem("Configuración", Icons.Outlined.Settings, false, isDarkTheme) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Settings.route)
                }
                DrawerMenuItem("Notificación", Icons.Outlined.Notifications, false, isDarkTheme) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Notifications.route)
                }

                Spacer(modifier = Modifier.weight(1f))

                // Logout button at bottom
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            sharedPreferences.edit().remove("accepted_terms").apply()
                            Toast.makeText(context, "Se cerró la sesión exitosamente", Toast.LENGTH_SHORT).show()
                            navController.navigate(Routes.Login.route)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFFFEBEE)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Logout,
                            contentDescription = "Logout",
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Cerrar Sesión",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFE53935)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        // BottomSheetScaffold state
        val sheetState = rememberBottomSheetScaffoldState()
        BottomSheetScaffold(
            scaffoldState = sheetState,
            sheetPeekHeight = 80.dp,
            sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            sheetContent = {
                // -- Start of bottom sheet Column --
                Column(
                    modifier = Modifier.fillMaxHeight(0.5f)
                ) {
                    // Solo un handle bar
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp)
                            .background(
                                Color.Gray.copy(alpha = 0.3f),
                                RoundedCornerShape(2.dp)
                            )
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Estacionamientos Cercanos",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                            )
                            Text(
                                text = "${filteredParkingSpots.size} lugares disponibles",
                                fontSize = 14.sp,
                                color = if (isDarkTheme) Color(0xFF90CAF9) else Color(0xFF4285F4)
                            )
                        }

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF4285F4).copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = selectedFilter,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF4285F4)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quick filters
                    LazyRow(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(listOf("Más cercano", "Más barato", "Mejor valorado", "24 horas")) { filter ->
                            FilterChip(
                                onClick = {
                                    selectedFilter = filter
                                    Toast.makeText(context, "Filtro: $filter", Toast.LENGTH_SHORT).show()
                                },
                                label = {
                                    Text(
                                        filter,
                                        fontSize = 12.sp,
                                        color = if (selectedFilter == filter) Color.White else Color(0xFF4285F4)
                                    )
                                },
                                selected = selectedFilter == filter,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF4285F4),
                                    containerColor = Color(0xFF4285F4).copy(alpha = 0.1f)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Parking list
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredParkingSpots) { parking ->
                            ParkingCard(
                                parking = parking,
                                isDarkTheme = isDarkTheme,
                                onClick = {
                                    selectedParking = parking
                                    showReservationDialog = true
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
                // -- End of bottom sheet Column --
            },
            sheetContainerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Full screen map
                if (hasLocationPermission) {
                    AndroidView(
                        factory = { ctx ->
                            val appContext = ctx.applicationContext
                            val map = MapView(appContext)
                            map.setTileSource(TileSourceFactory.MAPNIK)
                            map.setMultiTouchControls(true)
                            map.setUseDataConnection(true)

                            map.setZoomRounding(true)
                            map.minZoomLevel = 5.0
                            map.maxZoomLevel = 19.0
                            map.isTilesScaledToDpi = false
                            map.setScrollableAreaLimitDouble(null)
                            map.isHorizontalMapRepetitionEnabled = false
                            map.isVerticalMapRepetitionEnabled = false



                            val peruCenter = GeoPoint(-9.19, -75.0152)
                            map.controller.setZoom(6.5)
                            map.controller.setCenter(peruCenter)

                            val overlay = MyLocationNewOverlay(GpsMyLocationProvider(ctx), map)
                            overlay.enableMyLocation()
                            overlay.isDrawAccuracyEnabled = true

                            overlay.enableFollowLocation()


                            map.overlays.add(overlay)

                            parkingSpots.forEach { spot ->
                                if (spot.latitude != 0.0 && spot.longitude != 0.0) {
                                    val marker = org.osmdroid.views.overlay.Marker(map)
                                    marker.position = org.osmdroid.util.GeoPoint(spot.latitude, spot.longitude)
                                    marker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM)
                                    marker.title = spot.name
                                    marker.subDescription = spot.address
                                    map.overlays.add(marker)
                                }
                            }

                            map.invalidate()

                            val locationCallback = object : LocationCallback() {
                                override fun onLocationResult(result: LocationResult) {
                                    val location = result.lastLocation
                                    if (location != null) {
                                        val userLocation = GeoPoint(location.latitude, location.longitude)

                                        map.controller.setZoom(18.0)
                                        map.controller.setCenter(userLocation)

                                        val geocoder = Geocoder(ctx, Locale.getDefault())
                                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                        if (!addresses.isNullOrEmpty()) {
                                            district.value = addresses[0].locality ?: addresses[0].subAdminArea ?: ""
                                            city.value = addresses[0].adminArea ?: ""
                                            country.value = addresses[0].countryName ?: ""
                                        }
                                    }
                                }
                            }
                            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
                            val locationRequest = LocationRequest.create().apply {
                                interval = 5000
                                fastestInterval = 2000
                                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                                numUpdates = 3
                            }

                            val settingsClient = LocationServices.getSettingsClient(ctx)
                            val builder = com.google.android.gms.location.LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

                            settingsClient.checkLocationSettings(builder.build())
                                .addOnFailureListener {
                                    Toast.makeText(ctx, "Activa tu GPS para obtener ubicación en tiempo real", Toast.LENGTH_LONG).show()
                                }

                            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, ctx.mainLooper)
                            map
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                }

                // Top bar overlay
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(
                                onClick = { scope.launch { drawerState.open() } },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        Color(0xFF4285F4).copy(alpha = 0.1f),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "Menu",
                                    tint = Color(0xFF4285F4)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Dashboard Conductor",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                                )
                                AnimatedContent(
                                    targetState = district.value
                                ) { districtText ->
                                    Text(
                                        text = if (districtText.isNotEmpty()) "📍 $districtText" else "Obteniendo ubicación...",
                                        fontSize = 12.sp,
                                        color = if (isDarkTheme) Color(0xFF90CAF9) else Color(0xFF4285F4)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4285F4))
                                    .clickable {
                                        Toast.makeText(context, "Perfil de ${userName.value}", Toast.LENGTH_SHORT).show()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userName.value.take(1).uppercase(),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                // Floating action buttons
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 172.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FloatingActionButton(
                        onClick = {
                            showSearchDialog = true
                        },
                        containerColor = Color(0xFF4285F4),
                        contentColor = Color.White,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }

                    FloatingActionButton(
                        onClick = {
                            showFilterDialog = true
                        },
                        containerColor = if (isDarkTheme) Color(0xFF2C2C2C) else Color.White,
                        contentColor = Color(0xFF4285F4),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtros")
                    }

                    FloatingActionButton(
                        onClick = {
                            Toast.makeText(context, "Centrando en tu ubicación...", Toast.LENGTH_SHORT).show()
                        },
                        containerColor = if (isDarkTheme) Color(0xFF2C2C2C) else Color.White,
                        contentColor = Color(0xFF4285F4),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = "Mi ubicación")
                    }
                }
            }
        }
    }
}

@Composable
fun ParkingCard(
    parking: ParkingSpot,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) Color(0xFF2C2C2C) else Color(0xFFF8F9FA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = parking.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                    )
                    Text(
                        text = parking.address,
                        fontSize = 12.sp,
                        color = if (isDarkTheme) Color.LightGray else Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = parking.price,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4285F4)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = parking.rating.toString(),
                            fontSize = 12.sp,
                            color = if (isDarkTheme) Color.LightGray else Color.Gray,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = if (parking.availableSpots > 10) Color(0xFF4CAF50) else Color(0xFFFF9800),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${parking.availableSpots}/${parking.totalSpots} disponibles",
                        fontSize = 12.sp,
                        color = if (isDarkTheme) Color.LightGray else Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF4285F4),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = parking.distance,
                        fontSize = 12.sp,
                        color = if (isDarkTheme) Color.LightGray else Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            if (parking.features.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(parking.features.take(3)) { feature ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF4285F4).copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = feature,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 10.sp,
                                color = Color(0xFF4285F4)
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDialog(
    parking: ParkingSpot,
    isDarkTheme: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (duration: Int, vehicleType: String, startDateTime: String, placa: String) -> Unit
) {
    var selectedDuration by remember { mutableStateOf(1) }
    var selectedVehicle by remember { mutableStateOf("auto") }
    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }
    var placaVehiculo by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Cargar placa del usuario desde SharedPreferences
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("parkingnow_prefs", android.content.Context.MODE_PRIVATE)

    LaunchedEffect(Unit) {
        val savedPlaca = sharedPreferences.getString("user_placa", "") ?: ""
        placaVehiculo = savedPlaca
    }

    val pricePerHour = parking.price.replace("S/ ", "").replace("/hora", "").replace("/h", "").toDoubleOrNull() ?: 0.0
    val totalPrice = selectedDuration * pricePerHour

    val vehicleTypes = listOf("auto", "camioneta", "moto", "furgoneta", "bicicleta")
    val durations = listOf(1, 2, 3, 4, 6, 8, 12, 24)

    // Initialize with current date and time using Calendar (Peru timezone)
    val currentDateTime = remember {
        val calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("America/Lima"))
        val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())

        selectedDate = dateFormat.format(calendar.time)
        selectedTime = timeFormat.format(calendar.time)
        calendar
    }

    // DatePicker State
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    // TimePicker State
    val timePickerState = rememberTimePickerState(
        initialHour = currentDateTime.get(java.util.Calendar.HOUR_OF_DAY),
        initialMinute = currentDateTime.get(java.util.Calendar.MINUTE)
    )

    // Function to format the datetime for the API (Peru timezone, sin Z)
    fun formatDateTime(): String {
        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            try {
                val dateParts = selectedDate.split("/")
                val timeParts = selectedTime.split(":")

                val day = dateParts[0].padStart(2, '0')
                val month = dateParts[1].padStart(2, '0')
                val year = dateParts[2]
                val hour = timeParts[0].padStart(2, '0')
                val minute = timeParts[1].padStart(2, '0')

                // Crear Calendar con timezone Peru
                val calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("America/Lima"))
                calendar.set(year.toInt(), month.toInt() - 1, day.toInt(), hour.toInt(), minute.toInt(), 0)
                calendar.set(java.util.Calendar.MILLISECOND, 0)

                // Formatear fecha sin zona horaria Z
                val isoFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                isoFormat.timeZone = java.util.TimeZone.getTimeZone("America/Lima")
                return isoFormat.format(calendar.time)
            } catch (e: Exception) {
                // Fallback to current time if parsing fails
                val calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("America/Lima"))
                val isoFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                isoFormat.timeZone = java.util.TimeZone.getTimeZone("America/Lima")
                return isoFormat.format(calendar.time)
            }
        }
        val calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("America/Lima"))
        val isoFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        isoFormat.timeZone = java.util.TimeZone.getTimeZone("America/Lima")
        return isoFormat.format(calendar.time)
    }

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val calendar = java.util.Calendar.getInstance()
                            calendar.timeInMillis = millis
                            val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            selectedDate = dateFormat.format(calendar.time)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // Time Picker Dialog
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedTime = String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancelar")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Reservar Estacionamiento",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = parking.name,
                    fontSize = 16.sp,
                    color = Color(0xFF4285F4)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Date Selection
                Text(
                    text = "Fecha y Hora de Inicio",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF4285F4)
                        )
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Fecha",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(selectedDate.ifEmpty { "Fecha" }, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { showTimePicker = true },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF4285F4)
                        )
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = "Hora",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(selectedTime.ifEmpty { "Hora" }, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo para la placa del vehículo
                Text(
                    text = "Placa del Vehículo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = placaVehiculo,
                    onValueChange = {
                        placaVehiculo = it.uppercase()
                        // Guardar la placa en SharedPreferences para futuras reservas
                        sharedPreferences.edit().putString("user_placa", it.uppercase()).apply()
                    },
                    label = { Text("Ej: ABC-123") },
                    placeholder = { Text("Ingresa la placa") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.DirectionsCar,
                            contentDescription = "Placa",
                            tint = Color(0xFF4285F4)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4285F4),
                        focusedLabelColor = Color(0xFF4285F4)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tipo de Vehículo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vehicleTypes) { vehicle ->
                        FilterChip(
                            onClick = { selectedVehicle = vehicle },
                            label = {
                                Text(
                                    vehicle.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                                    },
                                    fontSize = 12.sp
                                )
                            },
                            selected = selectedVehicle == vehicle,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4285F4),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Duración (horas)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(durations) { duration ->
                        FilterChip(
                            onClick = { selectedDuration = duration },
                            label = { Text("${duration}h", fontSize = 12.sp) },
                            selected = selectedDuration == duration,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF4285F4),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4285F4).copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total a pagar:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                        )
                        Text(
                            text = "S/ ${String.format(Locale.getDefault(), "%.2f", totalPrice)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4285F4)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF4285F4)
                        )
                    ) {
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            val formattedDateTime = formatDateTime()
                            onConfirm(selectedDuration, selectedVehicle, formattedDateTime, placaVehiculo)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4285F4)
                        ),
                        enabled = selectedDate.isNotEmpty() && selectedTime.isNotEmpty() && placaVehiculo.isNotEmpty()
                    ) {
                        Text("Confirmar", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedDashboardCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconColor: Color,
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )

            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = if (isDarkTheme) Color.LightGray else Color.Gray
                )
            }
        }
    }
}

@Composable
fun DrawerMenuItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean = false,
    isDarkTheme: Boolean,
    onClick: () -> Unit
)  {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                if (isDarkTheme) Color(0xFF2D2D2D) else Color(0xFFE3F2FD)
            } else {
                Color.Transparent
            }
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp),
                tint = if (isSelected) {
                    if (isDarkTheme) Color.White else Color(0xFF1976D2)
                } else {
                    if (isDarkTheme) Color.LightGray else Color(0xFF4285F4)
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) {
                    if (isDarkTheme) Color.White else Color(0xFF1976D2)
                } else {
                    if (isDarkTheme) Color.White else Color(0xFF1E293B)
                }
            )
        }
    }
}
