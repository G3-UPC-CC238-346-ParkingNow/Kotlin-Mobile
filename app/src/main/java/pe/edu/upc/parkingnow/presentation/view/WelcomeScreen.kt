package pe.edu.upc.parkingnow.presentation.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.edu.upc.parkingnow.R
import pe.edu.upc.parkingnow.presentation.navigation.Routes
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset

@Composable
fun WelcomeScreen(navController: NavController) {
    val loginScreenContent = remember { mutableStateOf<@Composable () -> Unit>({ LoginScreen(navController = navController) }) }
    var showLogin by remember { mutableStateOf(false) }

    val alpha = remember { Animatable(1f) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        delay(4000)
        showLogin = true
        launch {
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 4000)
            )
        }
        launch {
            scale.animateTo(
                targetValue = 1.2f,
                animationSpec = tween(durationMillis = 4000)
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        loginScreenContent.value()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        alpha = alpha.value,
                        scaleX = scale.value,
                        scaleY = scale.value
                    )
            )
        }
    }
}