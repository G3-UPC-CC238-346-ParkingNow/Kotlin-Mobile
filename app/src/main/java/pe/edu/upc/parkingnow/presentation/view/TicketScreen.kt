package pe.edu.upc.parkingnow.presentation.view

import pe.edu.upc.parkingnow.presentation.navigation.Routes

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun TicketScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    Configuration.getInstance().load(context.applicationContext, context.getSharedPreferences("osmdroid", 0))

    var selectedParking by remember { mutableStateOf("Real Plaza Salaverry") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFF5F9FF))
            .padding(16.dp)
    ) {
        Text(
            text = "Selecciona tu espacio",
            fontSize = 22.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AndroidView(
            factory = { ctx ->
                val map = MapView(ctx.applicationContext)
                map.setTileSource(TileSourceFactory.MAPNIK)
                map.setMultiTouchControls(true)
                map.setUseDataConnection(true)

                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
                val locationRequest = LocationRequest.create().apply {
                    interval = 10000
                    fastestInterval = 5000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    numUpdates = 1
                }

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        val location = result.lastLocation
                        if (location != null) {
                            val userLocation = GeoPoint(location.latitude, location.longitude)
                            val marker = Marker(map).apply {
                                position = userLocation
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                title = "Estás aquí"
                            }
                            map.overlays.add(marker)
                            map.controller.setZoom(17.0)
                            map.controller.setCenter(userLocation)
                            Toast.makeText(ctx, "Ubicación detectada", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                if (ContextCompat.checkSelfPermission(
                        ctx,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, ctx.mainLooper)
                }

                map
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Opciones de estacionamiento", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        Column {
            listOf(
                Triple("Real Plaza Salaverry", "S/ 5.00 / hora", "⭐ 4.5"),
                Triple("Larcomar", "S/ 6.00 / hora", "⭐ 4.8"),
                Triple("Centro Cívico", "S/ 4.00 / hora", "⭐ 4.3")
            ).forEach { (nombre, precio, rating) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(
                            if (selectedParking == nombre) Color(0xFF0A4FFF) else Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedParking = nombre },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(nombre, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = if (selectedParking == nombre) Color.White else Color.Black)
                        Text(precio, color = if (selectedParking == nombre) Color.White else Color.Gray)
                        Text(rating, color = if (selectedParking == nombre) Color.White else Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate(Routes.Payment.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A4FFF))
        ) {
            Text("Pagar", fontSize = 16.sp, color = Color.White)
        }
    }
}
