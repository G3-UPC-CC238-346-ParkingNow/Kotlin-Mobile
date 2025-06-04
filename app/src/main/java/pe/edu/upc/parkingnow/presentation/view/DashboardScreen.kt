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

import pe.edu.upc.parkingnow.presentation.viewmodel.UserViewModel
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, userViewModel: UserViewModel, appViewModel: AppViewModel) {
    val currentUsername by userViewModel.username.collectAsState()
    val isDarkTheme = appViewModel.isDarkMode.collectAsState().value
    val context = LocalContext.current
    Configuration.getInstance().load(context.applicationContext, context.getSharedPreferences("osmdroid", 0))
    val sharedPreferences = context.getSharedPreferences("parkingnow_prefs", android.content.Context.MODE_PRIVATE)
    val hasAcceptedTerms = sharedPreferences.getBoolean("accepted_terms", false)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showTermsDialog by remember { mutableStateOf(!hasAcceptedTerms) }

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

    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    sharedPreferences.edit().putBoolean("accepted_terms", true).apply()
                    showTermsDialog = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Rechazar")
                }
            },
            title = {
                Text(text = "Términos y Condiciones", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "Al usar esta aplicación, aceptas los términos y condiciones. Tu ubicación será utilizada para mostrar estacionamientos cercanos y mejorar tu experiencia."
                )
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface
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
                                text = currentUsername.take(1).uppercase(),
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
                                text = currentUsername,
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
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White,
                        titleContentColor = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                        navigationIconContentColor = if (isDarkTheme) Color.White else Color(0xFF4285F4),
                        actionIconContentColor = if (isDarkTheme) Color.White else Color(0xFF4285F4)
                    ),
                    title = {
                        Column {
                            Text(
                                "Dashboard Conductor",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                "Gestiona tu estacionamiento",
                                fontSize = 12.sp,
                                color = if (isDarkTheme) Color.LightGray else Color.Gray
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4285F4))
                                .clickable { /* Profile action */ },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUsername.take(1).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .background(brush = backgroundGradient)
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Welcome section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4285F4)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "¡Bienvenido de vuelta!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Encuentra el estacionamiento perfecto",
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                // Quick actions section
                Text(
                    text = "Acciones Rápidas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EnhancedDashboardCard(
                        title = "Favoritos",
                        subtitle = "Lugares marcados",
                        icon = Icons.Default.Favorite,
                        backgroundColor = if (isDarkTheme) Color(0xFF2E7D32) else Color(0xFFE8F5E8),
                        iconColor = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f),
                        isDarkTheme = isDarkTheme
                    ) {
                        // Navigate to favorites
                    }

                    EnhancedDashboardCard(
                        title = "Ofertas",
                        subtitle = "Descuentos especiales",
                        icon = Icons.Default.LocalOffer,
                        backgroundColor = if (isDarkTheme) Color(0xFFEF6C00) else Color(0xFFFFF3E0),
                        iconColor = Color(0xFFFF9800),
                        modifier = Modifier.weight(1f),
                        isDarkTheme = isDarkTheme
                    ) {
                        // Navigate to offers
                    }
                }

                // Map section
                if (hasLocationPermission) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Mapa en Tiempo Real",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                                    )
                                    AnimatedContent(
                                        targetState = district.value
                                    ) { districtText ->
                                        Text(
                                            text = if (districtText.isNotEmpty()) "📍 $districtText" else "Obteniendo ubicación...",
                                            fontSize = 14.sp,
                                            color = if (isDarkTheme) Color(0xFF90CAF9) else Color(0xFF4285F4)
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE3F2FD)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = Color(0xFF4285F4),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isDarkTheme) Color(0xFF2A2A2A) else Color(0xFFF5F5F5))
                            ) {
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
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
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
                if (isDarkTheme) Color(0xFF3A3A3A) else Color(0xFFE3F2FD)
            } else {
                Color.Transparent
            }
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
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
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) {
                    if (isDarkTheme) Color.White else Color(0xFF1976D2)
                } else {
                    if (isDarkTheme) Color.White else Color(0xFF1E293B)
                }
            )
        }
    }
}