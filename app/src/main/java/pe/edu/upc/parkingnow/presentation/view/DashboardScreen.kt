package pe.edu.upc.parkingnow.presentation.view

import pe.edu.upc.parkingnow.presentation.navigation.Routes

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
fun DashboardScreen(navController: NavController, userViewModel: UserViewModel, appViewModel: AppViewModel) {
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
        userName.value = name
        userEmail.value = email
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

    // Sample parking data
    val parkingSpots = remember {
        listOf(
            ParkingSpot(
                id = "1",
                name = "Centro Comercial Jockey Plaza",
                address = "Av. Javier Prado Este 4200, Santiago de Surco",
                distance = "0.8 km",
                price = "S/ 5.00/hora",
                rating = 4.5f,
                features = listOf("Techado", "Seguridad 24h", "Cámaras"),
                availableSpots = 45,
                totalSpots = 200,
                isOpen24h = true,
                latitude = -12.0864,
                longitude = -76.9922
            ),
            ParkingSpot(
                id = "2",
                name = "Estacionamiento San Isidro",
                address = "Av. Conquistadores 145, San Isidro",
                distance = "1.2 km",
                price = "S/ 8.00/hora",
                rating = 4.8f,
                features = listOf("Valet Parking", "Lavado", "Techado"),
                availableSpots = 12,
                totalSpots = 50,
                isOpen24h = false,
                latitude = -12.0931,
                longitude = -77.0465
            ),
            ParkingSpot(
                id = "3",
                name = "Plaza Norte",
                address = "Av. Túpac Amaru 899, Independencia",
                distance = "2.1 km",
                price = "S/ 3.50/hora",
                rating = 4.2f,
                features = listOf("Económico", "Seguridad"),
                availableSpots = 78,
                totalSpots = 150,
                isOpen24h = true,
                latitude = -11.9889,
                longitude = -77.0611
            )
        )
    }

    val filteredParkingSpots = remember(searchQuery, selectedFilter) {
        parkingSpots.filter { spot ->
            val matchesSearch = spot.name.contains(searchQuery, ignoreCase = true) ||
                    spot.address.contains(searchQuery, ignoreCase = true)
            val matchesFilter = when (selectedFilter) {
                "Más cercano" -> true // Would sort by distance
                "Más barato" -> true // Would sort by price
                "Mejor valorado" -> spot.rating >= 4.5f
                "Disponible 24h" -> spot.isOpen24h
                else -> true
            }
            matchesSearch && matchesFilter
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
        ReservationDialog(
            parking = selectedParking!!,
            isDarkTheme = isDarkTheme,
            onDismiss = { showReservationDialog = false },
            onConfirm = { duration, vehicleType ->
                showReservationDialog = false
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

                            val locationCallback = object : LocationCallback() {
                                override fun onLocationResult(result: LocationResult) {
                                    val location = result.lastLocation
                                    if (location != null) {
                                        val userLocation = GeoPoint(location.latitude, location.longitude)

                                        map.overlays.clear()

                                        val overlay = MyLocationNewOverlay(GpsMyLocationProvider(ctx), map)
                                        overlay.enableMyLocation()
                                        overlay.isDrawAccuracyEnabled = true
                                        overlay.enableFollowLocation()

                                        map.overlays.add(overlay)

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

@Composable
fun ReservationDialog(
    parking: ParkingSpot,
    isDarkTheme: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (duration: Int, vehicleType: String) -> Unit
) {
    var selectedDuration by remember { mutableStateOf(1) }
    var selectedVehicle by remember { mutableStateOf("Automóvil") }

    val vehicleTypes = listOf("Automóvil", "SUV", "Motocicleta", "Camioneta")
    val durations = listOf(1, 2, 3, 4, 6, 8, 12, 24)

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
                modifier = Modifier.padding(24.dp)
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
                            label = { Text(vehicle, fontSize = 12.sp) },
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
                            text = "S/ ${(selectedDuration * 5.0).toInt()}.00",
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
                        onClick = { onConfirm(selectedDuration, selectedVehicle) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4285F4)
                        )
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
) {
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

