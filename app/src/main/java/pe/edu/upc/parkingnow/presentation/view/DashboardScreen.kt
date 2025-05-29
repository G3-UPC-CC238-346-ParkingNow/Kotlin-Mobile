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
import androidx.compose.material3.* // ✅ Esto ya incluye Text
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

import pe.edu.upc.parkingnow.presentation.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, userViewModel: UserViewModel) {
    // Use UserViewModel to persist username during the session
    val currentUsername by userViewModel.username.collectAsState()
    val context = LocalContext.current
    Configuration.getInstance().load(context.applicationContext, context.getSharedPreferences("osmdroid", 0))
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    // Estados separados para distrito, ciudad y país
    val district = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }
    val country = remember { mutableStateOf("") }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp),
                drawerContainerColor = Color(0xFFF6F6F6)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1D4ED8)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUsername.take(1).uppercase(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = currentUsername,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        AnimatedContent(
                            targetState = "${district.value}, ${city.value}, ${country.value}".trim(',', ' ')
                        ) { locationText ->
                            Text(locationText, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
                HorizontalDivider()
                Spacer(modifier = Modifier.height(40.dp))

                DrawerMenuItem("Inicio", Icons.Outlined.Home) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Dashboard.route)
                }
                Spacer(modifier = Modifier.height(12.dp))
                DrawerMenuItem("Reservas", Icons.Outlined.CalendarToday) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Bookings.route)
                }
                Spacer(modifier = Modifier.height(12.dp))
                DrawerMenuItem("Soporte", Icons.Outlined.SupportAgent) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Support.route)
                }
                Spacer(modifier = Modifier.height(12.dp))
                DrawerMenuItem("Seguimiento", Icons.Outlined.Place) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Tracking.route)
                }
                Spacer(modifier = Modifier.height(12.dp))
                DrawerMenuItem("Configuración", Icons.Outlined.Settings) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Settings.route)
                }
                Spacer(modifier = Modifier.height(12.dp))
                DrawerMenuItem("Notificación", Icons.Outlined.Notifications) {
                    scope.launch { drawerState.close() }
                    navController.navigate(Routes.Notifications.route)
                }
                Spacer(modifier = Modifier.height(12.dp))
                DrawerMenuItem("Logout", Icons.Outlined.Logout, onClick = {
                    Toast.makeText(context, "Se cerró la sesión exitosamente", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.Login.route)
                })
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White.copy(alpha = 0.9f),
                        titleContentColor = Color.Black,
                        navigationIconContentColor = Color.Black,
                        actionIconContentColor = Color.Black
                    ),
                    title = {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            Text("Dashboard Conductor", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            // Username removed as requested
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .padding(top = 12.dp)
                        )
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .background(Color(0xFFF8FBFF))
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                DashboardCard("Lugares marcados como favoritos")
                DashboardCard("Ofertas de estacionamientos")

                Spacer(modifier = Modifier.height(16.dp)) // Más espacio antes del mapa (reducido)

                if (hasLocationPermission) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mapa en tiempo real",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1E293B)
                        )

                        AnimatedContent(
                            targetState = district.value,
                            label = ""
                        ) {
                            Text(
                                text = it,
                                fontSize = 14.sp,
                                color = Color(0xFF1D4ED8)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(8.dp)
                    ) {
                        AndroidView(
                            factory = { ctx ->
                                val appContext = ctx.applicationContext
                                val map = MapView(appContext)
                                // 1. Set tile source and controls before changing zoom/center
                                map.setTileSource(TileSourceFactory.MAPNIK)
                                map.setMultiTouchControls(true)
                                map.setUseDataConnection(true)

                                // Mejoras de configuración de mapa
                                map.setZoomRounding(true)
                                map.minZoomLevel = 5.0
                                map.maxZoomLevel = 19.0
                                map.isTilesScaledToDpi = false
                                map.setScrollableAreaLimitDouble(null)
                                map.isHorizontalMapRepetitionEnabled = false
                                map.isVerticalMapRepetitionEnabled = false

                                // 1. Set default center and zoom to Perú before location
                                val peruCenter = GeoPoint(-9.19, -75.0152)
                                map.controller.setZoom(6.5)
                                map.controller.setCenter(peruCenter)

                                // 2. User location
                                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
                                val locationRequest = LocationRequest.create().apply {
                                    interval = 10000
                                    fastestInterval = 5000
                                    priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
                                    numUpdates = 1
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

                                            // Centrar el mapa directamente en la ubicación del usuario
                                            map.controller.setZoom(18.0)
                                            map.controller.setCenter(userLocation)

                                            val geocoder = Geocoder(ctx, Locale.getDefault())
                                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                            if (!addresses.isNullOrEmpty()) {
                                                district.value = addresses[0].subLocality ?: ""
                                                city.value = addresses[0].locality ?: addresses[0].subAdminArea ?: ""
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

                Spacer(modifier = Modifier.height(32.dp)) // Espacio final para separar del borde inferior
            }
        }
    }
}

@Composable
fun DashboardCard(text: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { /* onClick */ }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
        }
    }
}


@Composable
fun DrawerMenuItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(20.dp),
            tint = Color(0xFF1D4ED8)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF1D4ED8)
        )
    }
}