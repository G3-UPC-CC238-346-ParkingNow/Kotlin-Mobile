package pe.edu.upc.parkingnow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import pe.edu.upc.parkingnow.presentation.navigation.AppNavigation
import pe.edu.upc.parkingnow.ui.theme.ParkingNowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkingNowTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}