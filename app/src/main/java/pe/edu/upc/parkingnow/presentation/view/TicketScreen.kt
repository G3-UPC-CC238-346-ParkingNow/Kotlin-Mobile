package pe.edu.upc.parkingnow.presentation.view

import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import pe.edu.upc.parkingnow.R

import pe.edu.upc.parkingnow.presentation.navigation.Routes

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow

import kotlin.random.Random
data class ParkingOption(
    val name: String,
    val price: String,
    val rating: String,
    val ratingValue: Float,
    val availableSpots: Int = (5..30).random(),
    val distance: String = "${kotlin.random.Random.nextDouble(0.1, 3.0).let { String.format("%.1f", it) }} km"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(navController: NavController, appViewModel: AppViewModel) {
    val context = LocalContext.current
    val isDarkTheme by appViewModel.isDarkMode.collectAsState()
    val scrollState = rememberScrollState()

    // Configuración del mapa
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            context.applicationContext,
            context.getSharedPreferences("osmdroid", 0)
        )
    }

    var selectedParking by remember { mutableStateOf("Real Plaza Salaverry") }
    var searchQuery by remember { mutableStateOf("") }

    val parkingOptions = remember {
        listOf(
            ParkingOption("Real Plaza Salaverry", "S/ 5.00 / hora", "4.5", 4.5f, 12, "0.8 km"),
            ParkingOption("Larcomar", "S/ 6.00 / hora", "4.8", 4.8f, 8, "1.2 km"),
            ParkingOption("Centro Cívico", "S/ 4.00 / hora", "4.3", 4.3f, 15, "0.5 km")
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background image with overlay gradient
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Semi-transparent overlay for better text readability, dark mode compatible
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isDarkTheme) {
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF121212),
                                Color(0xFF121212)
                            )
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.85f),
                                Color.White.copy(alpha = 0.9f)
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
            // Top app bar
            TopAppBar(
                title = {
                    Text(
                        text = "Seleccionar Estacionamiento",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF4285F4),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
            ) {
                // Map section with title
                Text(
                    text = "Ubicación",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF1E293B),
                    modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
                )

                EnhancedMapSection()

                Spacer(modifier = Modifier.height(24.dp))

                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Buscar estacionamiento") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF4285F4)
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null,
                            tint = Color(0xFF4285F4)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFF4285F4),
                        unfocusedBorderColor = Color(0xFFBDBDBD)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Parking options section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Estacionamientos Cercanos",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkTheme) Color.White else Color(0xFF1E293B)
                    )

                    Text(
                        text = "Ver todos",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4285F4)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Enhanced parking options
                val filteredOptions = parkingOptions.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    filteredOptions.forEach { parking ->
                        EnhancedParkingOptionCard(
                            parking = parking,
                            isSelected = selectedParking == parking.name,
                            onClick = { selectedParking = parking.name },
                            isDarkTheme = isDarkTheme
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Payment button
                EnhancedPaymentButton(
                    onClick = {
                        val selected = parkingOptions.find { it.name == selectedParking }
                        selected?.let {
                            navController.currentBackStackEntry?.savedStateHandle?.set("selectedParkingName", it.name)
                            navController.currentBackStackEntry?.savedStateHandle?.set("selectedParkingPrice", it.price)
                            navController.navigate(Routes.Payment.route)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun EnhancedMapSection() {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    createMapView(ctx)
                },
                modifier = Modifier.fillMaxSize()
            )

            // Map controls overlay
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FloatingActionButton(
                    onClick = { /* Zoom in */ },
                    modifier = Modifier.size(40.dp),
                    containerColor = Color.White,
                    contentColor = Color(0xFF4285F4),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Acercar",
                        modifier = Modifier.size(20.dp)
                    )
                }

                FloatingActionButton(
                    onClick = { /* Zoom out */ },
                    modifier = Modifier.size(40.dp),
                    containerColor = Color.White,
                    contentColor = Color(0xFF4285F4),
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Alejar",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Current location button
            FloatingActionButton(
                onClick = { /* Center on user location */ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(48.dp),
                containerColor = Color(0xFF4285F4),
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MyLocation,
                    contentDescription = "Mi ubicación",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun EnhancedParkingOptionCard(
    parking: ParkingOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    isDarkTheme: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF4285F4)
            else if (isDarkTheme) Color(0xFF1E1E1E)
            else Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = parking.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White
                            else if (isDarkTheme) Color.White
                            else Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = if (isSelected) Color.White.copy(alpha = 0.8f) else Color(0xFF4285F4),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = parking.distance,
                            fontSize = 14.sp,
                            color = if (isSelected) Color.White.copy(alpha = 0.8f)
                                else if (isDarkTheme) Color.LightGray
                                else Color.Gray
                        )
                    }
                }

                // Rating badge
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color.White.copy(alpha = 0.2f) else Color(0xFFFFF8E1)
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (isSelected) Color.White else Color(0xFFFFB300),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = parking.rating,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFFFFB300)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider(
                color = if (isSelected) Color.White.copy(alpha = 0.2f) else Color(0xFFEEEEEE),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Price
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = parking.price.split(" / ").first(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White
                            else if (isDarkTheme) Color.White
                            else Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "/ hora",
                        fontSize = 14.sp,
                        color = if (isSelected) Color.White.copy(alpha = 0.8f)
                            else if (isDarkTheme) Color.LightGray
                            else Color.Gray
                    )
                }

                // Available spots
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocalParking,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else if (isDarkTheme) Color(0xFF81C784) else Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${parking.availableSpots} lugares disponibles",
                        fontSize = 14.sp,
                        color = if (isSelected) Color.White
                            else if (isDarkTheme) Color(0xFF81C784)
                            else Color(0xFF4CAF50)
                    )
                }
            }

            if (isSelected) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Seleccionado",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EnhancedPaymentButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4285F4)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Payment,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Continuar al Pago",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun createMapView(context: android.content.Context): MapView {
    val map = MapView(context.applicationContext).apply {
        setTileSource(TileSourceFactory.MAPNIK)
        setMultiTouchControls(true)
        setUseDataConnection(true)
        controller.setZoom(12.0)

        // Coordenadas por defecto (Lima, Perú - Centro)
        val defaultLocation = GeoPoint(-12.0464, -77.0428)
        controller.setCenter(defaultLocation)
    }

    // Configurar ubicación del usuario y marcadores
    setupUserLocation(context, map)

    return map
}

private fun setupUserLocation(context: android.content.Context, map: MapView) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        numUpdates = 1
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                val userLocation = GeoPoint(location.latitude, location.longitude)

                // Limpiar marcadores anteriores
                map.overlays.clear()

                // Agregar marcador del usuario
                val userMarker = Marker(map).apply {
                    position = userLocation
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Tu ubicación"
                    setOnMarkerClickListener { _, _ -> true } // evita crash por infoWindow nula
                }
                map.overlays.add(userMarker)

                // Centrar el mapa en la ubicación del usuario
                map.controller.setZoom(15.0)
                map.controller.setCenter(userLocation)

                // Agregar marcadores de estacionamientos cercanos
                addParkingMarkers(map)

                map.invalidate()
            }
        }
    }

    // Verificar permisos y solicitar ubicación
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            context.mainLooper
        )
    } else {
        // Si no hay permisos, mostrar marcadores por defecto
        addParkingMarkers(map)
    }
}

private fun addParkingMarkers(map: MapView) {
    // Más de 100 ubicaciones de estacionamiento por todo Lima
    val parkingLocations = listOf(
        // MIRAFLORES
        Triple("Larcomar", -12.1327, -77.0219),
        Triple("Óvalo Gutierrez", -12.1000, -77.0300),
        Triple("Miraflores Centro", -12.1197, -77.0285),
        Triple("Parque Kennedy", -12.1211, -77.0297),
        Triple("Malecón Miraflores", -12.1250, -77.0280),
        Triple("CC Miraflores", -12.1180, -77.0310),
        Triple("Av. Larco", -12.1190, -77.0290),
        Triple("Plaza Miraflores", -12.1205, -77.0295),
        Triple("Barranco Límite", -12.1300, -77.0250),
        Triple("Av. Benavides Miraflores", -12.1150, -77.0320),

        // SAN ISIDRO
        Triple("San Isidro Financial", -12.0975, -77.0364),
        Triple("Óvalo San Isidro", -12.0950, -77.0350),
        Triple("Country Club", -12.0900, -77.0380),
        Triple("Av. Javier Prado San Isidro", -12.0920, -77.0340),
        Triple("Bosque El Olivar", -12.0980, -77.0370),
        Triple("Centro Empresarial", -12.0960, -77.0355),
        Triple("Camino Real", -12.0940, -77.0365),
        Triple("Conquistadores", -12.0985, -77.0345),
        Triple("República de Panamá", -12.0930, -77.0375),
        Triple("Av. Arequipa San Isidro", -12.0970, -77.0360),

        // SURCO
        Triple("Jockey Plaza", -12.0889, -76.9775),
        Triple("Óvalo Higuereta", -12.0850, -76.9800),
        Triple("Av. Primavera", -12.0900, -76.9750),
        Triple("CC Chacarilla", -12.0920, -76.9720),
        Triple("Surco Centro", -12.0880, -76.9780),
        Triple("Av. Benavides Surco", -12.0860, -76.9790),
        Triple("La Molina Límite", -12.0800, -76.9700),
        Triple("Monterrico", -12.0870, -76.9760),
        Triple("Camacho", -12.0890, -76.9740),
        Triple("Higuereta", -12.0840, -76.9810),

        // LA MOLINA
        Triple("Megaplaza La Molina", -12.0750, -76.9650),
        Triple("PUCP", -12.0690, -76.9550),
        Triple("Av. Raúl Ferrero", -12.0720, -76.9600),
        Triple("Rinconada", -12.0680, -76.9580),
        Triple("Sol de La Molina", -12.0760, -76.9620),
        Triple("Cieneguilla Límite", -12.0650, -76.9500),
        Triple("La Fontana", -12.0700, -76.9570),
        Triple("Av. La Molina", -12.0740, -76.9640),
        Triple("Musa", -12.0710, -76.9590),
        Triple("Villa María", -12.0730, -76.9610),

        // SAN MIGUEL
        Triple("Plaza San Miguel", -12.0775, -77.0911),
        Triple("Av. La Marina", -12.0800, -77.0950),
        Triple("Óvalo Dos de Mayo", -12.0750, -77.0900),
        Triple("Av. Universitaria San Miguel", -12.0780, -77.0920),
        Triple("Pueblo Libre Límite", -12.0760, -77.0880),
        Triple("Magdalena Límite", -12.0790, -77.0940),
        Triple("Bertolotto", -12.0770, -77.0910),
        Triple("Av. Sucre", -12.0785, -77.0925),
        Triple("Parque San Miguel", -12.0775, -77.0915),
        Triple("CC San Miguel", -12.0765, -77.0905),

        // PUEBLO LIBRE
        Triple("Museo Nacional", -12.0740, -77.0860),
        Triple("Av. Brasil", -12.0720, -77.0840),
        Triple("Plaza Bolívar Pueblo Libre", -12.0730, -77.0850),
        Triple("Av. Universitaria Pueblo Libre", -12.0750, -77.0870),
        Triple("Jesús María Límite", -12.0710, -77.0820),
        Triple("Magdalena Límite PL", -12.0760, -77.0880),
        Triple("Cuadra 20 Brasil", -12.0725, -77.0845),
        Triple("Vivanco", -12.0735, -77.0855),
        Triple("Parque Pueblo Libre", -12.0745, -77.0865),
        Triple("San Miguel Límite PL", -12.0755, -77.0875),

        // JESÚS MARÍA
        Triple("Campo de Marte", -12.0650, -77.0450),
        Triple("Av. Brasil Jesús María", -12.0680, -77.0480),
        Triple("Hospital Militar", -12.0670, -77.0460),
        Triple("Av. Salaverry JM", -12.0660, -77.0470),
        Triple("Lince Límite", -12.0640, -77.0440),
        Triple("Pueblo Libre Límite JM", -12.0690, -77.0490),
        Triple("Av. 28 de Julio", -12.0675, -77.0465),
        Triple("Parque Jesús María", -12.0665, -77.0475),
        Triple("Av. Pershing", -12.0655, -77.0455),
        Triple("CC Jesús María", -12.0685, -77.0485),

        // LINCE
        Triple("Av. Arequipa Lince", -12.0580, -77.0420),
        Triple("Parque Mariscal Castilla", -12.0590, -77.0430),
        Triple("Av. Arenales", -12.0570, -77.0410),
        Triple("La Victoria Límite", -12.0560, -77.0400),
        Triple("Jesús María Límite Lince", -12.0600, -77.0440),
        Triple("Av. Petit Thouars", -12.0585, -77.0425),
        Triple("Óvalo Lince", -12.0575, -77.0415),
        Triple("Parque Lince", -12.0595, -77.0435),
        Triple("Av. Inca Garcilaso", -12.0565, -77.0405),
        Triple("CC Lince", -12.0605, -77.0445),

        // MAGDALENA
        Triple("Av. Brasil Magdalena", -12.0850, -77.0750),
        Triple("Playa Magdalena", -12.0900, -77.0800),
        Triple("San Miguel Límite Mag", -12.0820, -77.0720),
        Triple("Pueblo Libre Límite Mag", -12.0830, -77.0730),
        Triple("Av. Javier Prado Mag", -12.0860, -77.0760),
        Triple("Parque Magdalena", -12.0840, -77.0740),
        Triple("Av. Costanera", -12.0880, -77.0780),
        Triple("CC Magdalena", -12.0870, -77.0770),
        Triple("Av. Pershing Mag", -12.0855, -77.0755),
        Triple("Plaza Magdalena", -12.0845, -77.0745),

        // CENTRO DE LIMA
        Triple("Plaza de Armas", -12.0464, -77.0428),
        Triple("Centro Cívico", -12.0581, -77.0364),
        Triple("Congreso", -12.0470, -77.0430),
        Triple("Palacio de Gobierno", -12.0460, -77.0425),
        Triple("Jr. de la Unión", -12.0480, -77.0440),
        Triple("Mercado Central", -12.0500, -77.0450),
        Triple("Estación Central", -12.0520, -77.0460),
        Triple("Av. Abancay", -12.0540, -77.0470),
        Triple("Plaza San Martín", -12.0490, -77.0435),
        Triple("Jr. Lampa", -12.0475, -77.0432),

        // BREÑA
        Triple("Av. Brasil Breña", -12.0600, -77.0500),
        Triple("Av. Arenales Breña", -12.0620, -77.0520),
        Triple("Lima Límite Breña", -12.0580, -77.0480),
        Triple("Jesús María Límite Breña", -12.0640, -77.0540),
        Triple("Parque Breña", -12.0610, -77.0510),
        Triple("Av. Venezuela", -12.0590, -77.0490),
        Triple("Av. Bolivia", -12.0630, -77.0530),
        Triple("CC Breña", -12.0615, -77.0515),
        Triple("Plaza Breña", -12.0605, -77.0505),
        Triple("Av. Tingo María", -12.0625, -77.0525),

        // LA VICTORIA
        Triple("Gamarra", -12.0700, -77.0200),
        Triple("Av. 28 de Julio La Victoria", -12.0720, -77.0220),
        Triple("Mercado La Parada", -12.0680, -77.0180),
        Triple("Av. Iquitos", -12.0740, -77.0240),
        Triple("San Luis Límite", -12.0660, -77.0160),
        Triple("Lince Límite LV", -12.0760, -77.0260),
        Triple("Parque La Victoria", -12.0710, -77.0210),
        Triple("Av. México", -12.0730, -77.0230),
        Triple("CC La Victoria", -12.0690, -77.0190),
        Triple("Plaza La Victoria", -12.0750, -77.0250),

        // SAN LUIS
        Triple("Av. Del Aire", -12.0640, -77.0140),
        Triple("La Victoria Límite SL", -12.0660, -77.0160),
        Triple("Ate Límite", -12.0620, -77.0120),
        Triple("Parque San Luis", -12.0650, -77.0150),
        Triple("Av. Las Torres", -12.0630, -77.0130),
        Triple("CC San Luis", -12.0645, -77.0145),
        Triple("Plaza San Luis", -12.0635, -77.0135),
        Triple("Av. Nicolás Arriola", -12.0655, -77.0155),
        Triple("Mercado San Luis", -12.0625, -77.0125),
        Triple("Av. Javier Prado SL", -12.0665, -77.0165),

        // CALLAO
        Triple("Plaza Norte", -11.9422, -77.0631),
        Triple("Aeropuerto Jorge Chávez", -12.0219, -77.1143),
        Triple("Puerto del Callao", -12.0500, -77.1400),
        Triple("Av. Faucett", -12.0000, -77.1000),
        Triple("Bellavista", -12.0550, -77.1150),
        Triple("La Perla", -12.0600, -77.1100),
        Triple("Carmen de la Legua", -12.0400, -77.1200),
        Triple("Ventanilla", -11.8800, -77.1500),
        Triple("Mi Perú", -11.9200, -77.1300),
        Triple("CC Callao", -12.0300, -77.1250),

        // OTROS DISTRITOS
        Triple("Real Plaza Salaverry", -12.0464, -77.0428),
        Triple("Mall del Sur", -12.1656, -76.9831),
        Triple("Óvalo Monitor", -12.1100, -77.0100),
        Triple("Av. Javier Prado Este", -12.0850, -76.9500),
        Triple("Av. Angamos", -12.1300, -77.0200),
        Triple("Barranco Centro", -12.1400, -77.0180),
        Triple("Chorrillos Centro", -12.1700, -77.0150),
        Triple("Villa El Salvador", -12.2100, -76.9400),
        Triple("San Juan de Miraflores", -12.1600, -76.9700),
        Triple("Villa María del Triunfo", -12.1800, -76.9500)
    )

    parkingLocations.forEach { (name, lat, lon) ->
        val marker = Marker(map).apply {
            position = GeoPoint(lat, lon)
            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            title = name
            snippet = "Estacionamiento disponible"
            infoWindow = null
        }
        map.overlays.add(marker)
    }
}