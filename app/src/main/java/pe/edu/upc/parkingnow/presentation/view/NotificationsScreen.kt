package pe.edu.upc.parkingnow.presentation.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.vector.ImageVector
import pe.edu.upc.parkingnow.R
import pe.edu.upc.parkingnow.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(navController: NavController, appViewModel: AppViewModel) {
    val context = LocalContext.current
    val isDarkMode = appViewModel.isDarkMode.collectAsState().value
    val scrollState = rememberScrollState()

    val backgroundColor = if (isDarkMode)
        Color(0xFF121212)
    else
        Color(0xFFF5F9FF)

    val cardColor = if (isDarkMode)
        Color(0xFF1E1E1E)
    else
        Color.White

    val textColor = if (isDarkMode)
        Color.White
    else
        Color(0xFF1E293B)

    val secondaryTextColor = if (isDarkMode)
        Color.LightGray
    else
        Color.Gray

    val accentColor = Color(0xFF4285F4)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Background image with overlay gradient (only in light mode)
        if (!isDarkMode) {
            Image(
                painter = painterResource(id = R.drawable.login_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Semi-transparent overlay for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.7f),
                                Color.White.copy(alpha = 0.85f)
                            )
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Top app bar
            TopAppBar(
                title = {
                    Text(
                        text = "Notificaciones",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.Dashboard.route) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            tint = accentColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Toggle notifications settings */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configuración de notificaciones",
                            tint = accentColor
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
                    .padding(bottom = 100.dp) // Space for bottom navigation
            ) {
                // Today's notifications section
                NotificationSection(
                    title = "Hoy",
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    accentColor = accentColor
                ) {
                    NotificationItem(
                        icon = Icons.Default.Receipt,
                        title = "Nuevo recibo disponible",
                        description = "Tu recibo por el estacionamiento en Real Plaza Salaverry está disponible",
                        time = "Hace 2 horas",
                        isUnread = true,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        accentColor = accentColor,
                        onClick = {
                            Toast.makeText(context, "Mostrando recibo", Toast.LENGTH_SHORT).show()
                        }
                    )

                    NotificationItem(
                        icon = Icons.Default.Place,
                        title = "Estacionamiento disponible",
                        description = "Hay espacios disponibles en tu estacionamiento favorito: Larcomar",
                        time = "Hace 5 horas",
                        isUnread = true,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        accentColor = accentColor,
                        onClick = {
                            Toast.makeText(context, "Mostrando estacionamiento", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Earlier notifications section
                NotificationSection(
                    title = "Anteriores",
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    accentColor = accentColor
                ) {
                    NotificationItem(
                        icon = Icons.Default.LocalOffer,
                        title = "Oferta especial",
                        description = "50% de descuento en tu próxima reserva en Centro Cívico",
                        time = "Ayer",
                        isUnread = false,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        accentColor = accentColor,
                        onClick = {
                            Toast.makeText(context, "Mostrando oferta", Toast.LENGTH_SHORT).show()
                        }
                    )

                    NotificationItem(
                        icon = Icons.Default.Timer,
                        title = "Recordatorio de reserva",
                        description = "Tu reserva en Jockey Plaza expira en 30 minutos",
                        time = "Ayer",
                        isUnread = false,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        accentColor = accentColor,
                        onClick = {
                            Toast.makeText(context, "Mostrando recordatorio", Toast.LENGTH_SHORT).show()
                        }
                    )

                    NotificationItem(
                        icon = Icons.Default.Star,
                        title = "Califica tu experiencia",
                        description = "¿Cómo fue tu experiencia en el estacionamiento de Real Plaza Salaverry?",
                        time = "2 días atrás",
                        isUnread = false,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryTextColor = secondaryTextColor,
                        accentColor = accentColor,
                        onClick = {
                            Toast.makeText(context, "Calificando experiencia", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Categories section
                Text(
                    text = "Categorías",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryButton(
                        icon = Icons.Default.Receipt,
                        label = "Recibos",
                        cardColor = cardColor,
                        accentColor = accentColor,
                        textColor = textColor,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            Toast.makeText(context, "Mostrando recibos", Toast.LENGTH_SHORT).show()
                        }
                    )

                    CategoryButton(
                        icon = Icons.Default.Place,
                        label = "Favoritos",
                        cardColor = cardColor,
                        accentColor = accentColor,
                        textColor = textColor,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            Toast.makeText(context, "Mostrando favoritos", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CategoryButton(
                        icon = Icons.Default.LocalOffer,
                        label = "Ofertas",
                        cardColor = cardColor,
                        accentColor = accentColor,
                        textColor = textColor,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            Toast.makeText(context, "Mostrando ofertas", Toast.LENGTH_SHORT).show()
                        }
                    )

                    CategoryButton(
                        icon = Icons.Default.Notifications,
                        label = "Todas",
                        cardColor = cardColor,
                        accentColor = accentColor,
                        textColor = textColor,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            Toast.makeText(context, "Mostrando todas las notificaciones", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Settings card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isDarkMode) Color(0xFF2C2C2E) else Color(0xFFE3F2FD)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Configurar notificaciones",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Text(
                                text = "Personaliza qué notificaciones quieres recibir",
                                fontSize = 14.sp,
                                color = secondaryTextColor
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = secondaryTextColor
                        )
                    }
                }
            }
        }

        // Bottom navigation
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = cardColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                tonalElevation = 0.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.Dashboard.route) },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Home,
                            contentDescription = "Inicio",
                            tint = secondaryTextColor
                        )
                    },
                    label = {
                        Text(
                            "Inicio",
                            color = secondaryTextColor,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )

                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already on notifications */ },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notificaciones",
                            tint = accentColor
                        )
                    },
                    label = {
                        Text(
                            "Notificaciones",
                            color = accentColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Routes.Settings.route) },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Ajustes",
                            tint = secondaryTextColor
                        )
                    },
                    label = {
                        Text(
                            "Ajustes",
                            color = secondaryTextColor,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent)
                )
            }
        }
    }
}

@Composable
fun NotificationSection(
    title: String,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    accentColor: Color,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        content()
    }
}

@Composable
fun NotificationItem(
    icon: ImageVector,
    title: String,
    description: String,
    time: String,
    isUnread: Boolean,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnread) 4.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isUnread)
                            accentColor.copy(alpha = 0.1f)
                        else
                            secondaryTextColor.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isUnread) accentColor else secondaryTextColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = if (isUnread) FontWeight.Bold else FontWeight.Medium,
                    color = if (isUnread) accentColor else textColor
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = secondaryTextColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = secondaryTextColor.copy(alpha = 0.7f)
                )
            }

            // Unread indicator
            if (isUnread) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
            }
        }
    }
}

@Composable
fun CategoryButton(
    icon: ImageVector,
    label: String,
    cardColor: Color,
    accentColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
        }
    }
}