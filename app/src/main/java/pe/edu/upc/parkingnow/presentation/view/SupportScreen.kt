package pe.edu.upc.parkingnow.presentation.view

import pe.edu.upc.parkingnow.presentation.navigation.Routes

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import pe.edu.upc.parkingnow.R
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(navController: NavController, appViewModel: AppViewModel) {
    val isDarkMode = appViewModel.isDarkMode.collectAsState().value
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Estado para los campos de texto
    val problema = remember { mutableStateOf("") }
    val comentario = remember { mutableStateOf("") }

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
                        text = "Centro de Soporte",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Routes.Dashboard.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = accentColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(accentColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SupportAgent,
                            contentDescription = "Soporte",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
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
                // Welcome message card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode) Color(0xFF1A1A2E) else Color(0xFFF0F8FF)
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
                                .background(accentColor.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SupportAgent,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "¡Estamos aquí para ayudarte!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                            Text(
                                text = "Describe tu problema o envíanos tus sugerencias",
                                fontSize = 14.sp,
                                color = secondaryTextColor
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Quick help options
                Text(
                    text = "Ayuda Rápida",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    QuickHelpCard(
                        icon = Icons.Default.Help,
                        title = "FAQ",
                        description = "Preguntas frecuentes",
                        cardColor = cardColor,
                        textColor = textColor,
                        accentColor = accentColor,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            Toast.makeText(context, "Abriendo FAQ", Toast.LENGTH_SHORT).show()
                        }
                    )

                    QuickHelpCard(
                        icon = Icons.Default.Chat,
                        title = "Chat",
                        description = "Chat en vivo",
                        cardColor = cardColor,
                        textColor = textColor,
                        accentColor = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            Toast.makeText(context, "Iniciando chat", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Problem report section
                SupportSection(
                    title = "Reportar un Problema",
                    description = "Describe detalladamente el problema que estás experimentando",
                    icon = Icons.Default.BugReport,
                    textFieldValue = problema.value,
                    onTextFieldChange = { problema.value = it },
                    textFieldLabel = "Describe tu problema aquí...",
                    buttonText = "Enviar Reporte",
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    accentColor = Color(0xFFFF5722),
                    onButtonClick = {
                        if (problema.value.isNotBlank()) {
                            Toast.makeText(context, "Reporte enviado exitosamente", Toast.LENGTH_SHORT).show()
                            problema.value = ""
                        } else {
                            Toast.makeText(context, "Por favor describe el problema", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Feedback section
                SupportSection(
                    title = "Comentarios y Sugerencias",
                    description = "Ayúdanos a mejorar compartiendo tus ideas y comentarios",
                    icon = Icons.Default.Feedback,
                    textFieldValue = comentario.value,
                    onTextFieldChange = { comentario.value = it },
                    textFieldLabel = "Comparte tus comentarios o sugerencias...",
                    buttonText = "Enviar Comentario",
                    cardColor = cardColor,
                    textColor = textColor,
                    secondaryTextColor = secondaryTextColor,
                    accentColor = accentColor,
                    onButtonClick = {
                        if (comentario.value.isNotBlank()) {
                            Toast.makeText(context, "Comentario enviado exitosamente", Toast.LENGTH_SHORT).show()
                            comentario.value = ""
                        } else {
                            Toast.makeText(context, "Por favor escribe tu comentario", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Contact information card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContactSupport,
                                contentDescription = null,
                                tint = accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Información de Contacto",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        ContactItem(
                            icon = Icons.Default.Email,
                            label = "Email",
                            value = "soporte@parkingnow.com",
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            accentColor = accentColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ContactItem(
                            icon = Icons.Default.Phone,
                            label = "Teléfono",
                            value = "+51 1 234-5678",
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            accentColor = accentColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ContactItem(
                            icon = Icons.Default.Schedule,
                            label = "Horario de atención",
                            value = "Lunes a Viernes: 8:00 AM - 6:00 PM",
                            textColor = textColor,
                            secondaryTextColor = secondaryTextColor,
                            accentColor = accentColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Rating card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode) Color(0xFF2D1B69) else Color(0xFFFFF3E0)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "¿Te gustó nuestro servicio?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "Califica tu experiencia con nuestro soporte",
                            fontSize = 14.sp,
                            color = secondaryTextColor,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                Toast.makeText(context, "Abriendo calificación", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFC107)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Calificar",
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
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
                    onClick = { /* Already on support */ },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.SupportAgent,
                            contentDescription = "Soporte",
                            tint = accentColor
                        )
                    },
                    label = {
                        Text(
                            "Soporte",
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
fun QuickHelpCard(
    icon: ImageVector,
    title: String,
    description: String,
    cardColor: Color,
    textColor: Color,
    accentColor: Color,
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
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Text(
                text = description,
                fontSize = 12.sp,
                color = textColor.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SupportSection(
    title: String,
    description: String,
    icon: ImageVector,
    textFieldValue: String,
    onTextFieldChange: (String) -> Unit,
    textFieldLabel: String,
    buttonText: String,
    cardColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    accentColor: Color,
    onButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(accentColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = secondaryTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = textFieldValue,
                onValueChange = onTextFieldChange,
                label = { Text(textFieldLabel) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                minLines = 3,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    focusedLabelColor = accentColor
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onButtonClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buttonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ContactItem(
    icon: ImageVector,
    label: String,
    value: String,
    textColor: Color,
    secondaryTextColor: Color,
    accentColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = accentColor,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 12.sp,
                color = secondaryTextColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = textColor
            )
        }
    }
}