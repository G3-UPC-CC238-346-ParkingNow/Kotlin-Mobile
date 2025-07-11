package pe.edu.upc.parkingnow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.upc.parkingnow.presentation.navigation.AppNavigation
import pe.edu.upc.parkingnow.presentation.viewmodel.UserViewModel
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel
import pe.edu.upc.parkingnow.presentation.viewmodel.ReservationViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import pe.edu.upc.parkingnow.ui.theme.ParkingNowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel: AppViewModel = viewModel()
            val isDarkModeEnabled by appViewModel.isDarkMode.collectAsState()
            ParkingNowTheme(isDarkModeEnabled = isDarkModeEnabled) {
                val navController = rememberNavController()
                val userViewModel: UserViewModel = viewModel()
                val reservationViewModel: ReservationViewModel = viewModel()
                AppNavigation(
                    navController = navController,
                    userViewModel = userViewModel,
                    appViewModel = appViewModel,
                    reservationViewModel = reservationViewModel
                )
            }
        }
    }
}

