package pe.edu.upc.parkingnow.presentation.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController, username: String) {
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
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(username, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Lima, Perú", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                HorizontalDivider()
                Spacer(modifier = Modifier.height(40.dp))

                DrawerMenuItem("Inicio", Icons.Default.Home) { /* No navigation */ }
                Spacer(modifier = Modifier.height(12.dp))
                DrawerMenuItem("Reservas", Icons.Default.Book) {
                    scope.launch { drawerState.close() }
                    navController.navigate("bookings")
                }
                Spacer(modifier = Modifier.height(12.dp))
                DrawerMenuItem("Soporte", Icons.Default.Support) {
                    scope.launch { drawerState.close() }
                    navController.navigate("support")
                }
                Spacer(modifier = Modifier.height(12.dp))
                DrawerMenuItem("Seguimiento", Icons.Default.LocationOn) {
                    scope.launch { drawerState.close() }
                    navController.navigate("tracking")
                }
                Spacer(modifier = Modifier.height(12.dp))
                DrawerMenuItem("Configuración", Icons.Default.Settings) {
                    scope.launch { drawerState.close() }
                    navController.navigate("settings")
                }
                Spacer(modifier = Modifier.height(12.dp))
                DrawerMenuItem("Notificación", Icons.Default.Notifications) {
                    scope.launch { drawerState.close() }
                    navController.navigate("notifications")
                }

                Spacer(modifier = Modifier.height(40.dp))

                DrawerMenuItem("Logout", Icons.Default.ExitToApp, onClick = {
                    Toast.makeText(context, "Se cerró la sesión exitosamente", Toast.LENGTH_SHORT).show()
                    navController.navigate("login")
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
                    title = { Text("Dashboard Conductor") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "User",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DashboardCard("Lugares marcados como favoritos")
                DashboardCard("Ofertas de estacionamientos")

                Spacer(modifier = Modifier.height(48.dp)) // Más espacio antes del mapa

                if (hasLocationPermission) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp) // Altura aumentada para mejor visualización
                            .padding(horizontal = 8.dp, vertical = 16.dp) // Padding mejorado
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
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { /* onClick */ }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
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
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 14.sp
        )
    }
}