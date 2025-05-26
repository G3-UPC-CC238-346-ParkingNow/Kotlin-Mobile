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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
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
                        Text("John Smith", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Jakarta, Indonesia", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                HorizontalDivider()

                val menuItems = listOf(
                    "Inicio", "Reservas", "Soporte", "Seguimiento", "Configuración", "Notificación"
                )

                menuItems.forEach { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch { drawerState.close() }
                                when (item) {
                                    "Reservas" -> navController.navigate("bookings")
                                    "Soporte" -> navController.navigate("support")
                                    "Seguimiento" -> navController.navigate("tracking")
                                    "Configuración" -> navController.navigate("settings")
                                    "Notificación" -> navController.navigate("notifications")
                                }
                            }
                            .padding(16.dp),
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Logout",
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Toast.makeText(context, "Se cerró la sesión exitosamente", Toast.LENGTH_SHORT).show()
                            navController.navigate("login")
                        }
                        .padding(16.dp)
                )
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DashboardCard("Lugares marcados como favoritos")
                DashboardCard("Ofertas de estacionamientos")
                if (hasLocationPermission) {
                    AndroidView(
                        factory = { ctx ->
                            val appContext = ctx.applicationContext
                            val map = MapView(appContext)
                            map.setTileSource(TileSourceFactory.MAPNIK)
                            map.setMultiTouchControls(true)
                            map.setUseDataConnection(true) // Asegurar que use red

                            // Limitar el área visible al rango aproximado de Perú
                            // map.setScrollableAreaLimitLatitude(-0.0, -18.5, 0)
                            // map.setScrollableAreaLimitLongitude(-69.0, -81.5, 0)

                            // Listener para advertir si el usuario se aleja más allá del límite
                            map.addMapListener(object : org.osmdroid.events.MapListener {
                                override fun onScroll(event: org.osmdroid.events.ScrollEvent?): Boolean = false
                                override fun onZoom(event: org.osmdroid.events.ZoomEvent?): Boolean {
                                    if (map.zoomLevelDouble < 4.0) {
                                        Toast.makeText(ctx, "Límite del mapa alcanzado", Toast.LENGTH_SHORT).show()
                                    }
                                    return false
                                }
                            })

                            // Mostrar todo Perú por defecto
                            val peruCenter = GeoPoint(-9.19, -75.0152)
                            map.controller.setZoom(5.5)
                            map.controller.setCenter(peruCenter)

                            // Obtener ubicación en tiempo real y centrar el mapa
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
                                        val marker = org.osmdroid.views.overlay.Marker(map)
                                        marker.position = userLocation
                                        marker.setAnchor(org.osmdroid.views.overlay.Marker.ANCHOR_CENTER, org.osmdroid.views.overlay.Marker.ANCHOR_BOTTOM)
                                        marker.title = "Estás aquí"
                                        map.overlays.add(marker)
                                        map.controller.setZoom(17.0)
                                        map.controller.setCenter(userLocation)
                                        Toast.makeText(ctx, "Tu ubicación actual ha sido detectada", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, ctx.mainLooper)

                            map
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
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
