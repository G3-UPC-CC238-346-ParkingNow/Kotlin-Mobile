package pe.upc.parkingnow.presentation.dashboarddriver

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import kotlinx.coroutines.launch
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.upc.parkingnow.R

@Composable
fun DashboardDriverScreen(
    state: DashboardDriverState = DashboardDriverState(),
    onFavoritePlacesClick: () -> Unit = {},
    onWeeklyOffersClick: () -> Unit = {},
    onMapClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                state = state,
                onMenuClick = { /* Drawer ya está abierto */ },
                onLogout = onLogout
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F6FA))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 26.dp, vertical = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título, menú y avatar
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { scope.launch { drawerState.open() } },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color(0xFF232943))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Dashboard Conductor",
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp,
                        color = Color(0xFF232943),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = state.profileImage ?: R.drawable.conductor),
                        contentDescription = "Avatar Conductor",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(25.dp))

                // Botón: Lugares favoritos
                Text(
                    text = "Reserva en tus lugares favoritos",
                    fontSize = 14.sp,
                    color = Color(0xFF232943),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Start)
                )
                Button(
                    onClick = onFavoritePlacesClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF232943)
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp)
                ) {
                    Text("Lugares marcados como favoritos", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }

                // Botón: Ofertas Semanales
                Text(
                    text = "Ofertas Semanales",
                    fontSize = 14.sp,
                    color = Color(0xFF232943),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 12.dp)
                )
                Button(
                    onClick = onWeeklyOffersClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF232943)
                    ),
                    elevation = ButtonDefaults.buttonElevation(2.dp)
                ) {
                    Text("Ofertas de estacionamientos", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }

                // Mapa o próxima reserva
                Text(
                    text = "Reserva algo ya",
                    fontSize = 14.sp,
                    color = Color(0xFF232943),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 16.dp, bottom = 10.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.White)
                        .padding(12.dp)
                        .clickable { onMapClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Mapa de Google Maps",
                        color = Color(0xFFB3B9C9),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerContent(
    state: DashboardDriverState,
    onMenuClick: () -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .background(Color.White)
            .padding(vertical = 30.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Usuario
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 18.dp)
        ) {
            Image(
                painter = painterResource(id = state.profileImage ?: R.drawable.conductor),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("John Smith", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = Color(0xFF232943))
                Text("Lima, Perú", fontSize = 13.sp, color = Color(0xFF838A9C))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        DrawerMenuItem("Inicio")
        DrawerMenuItem("Reservas")
        DrawerMenuItem("Soporte")
        DrawerMenuItem("Seguimiento")
        DrawerMenuItem("Configuración")
        DrawerMenuItem("Notificación")

        Spacer(modifier = Modifier.weight(1f))
        Divider()
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Logout",
            color = Color(0xFFD80027),
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .clickable { onLogout() }
                .padding(vertical = 10.dp, horizontal = 12.dp)
        )
    }
}

@Composable
private fun DrawerMenuItem(
    label: String,
    onClick: () -> Unit = {}
) {
    Text(
        text = label,
        fontSize = 15.sp,
        color = Color(0xFF232943),
        fontWeight = FontWeight.Medium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 18.dp)
    )
}

