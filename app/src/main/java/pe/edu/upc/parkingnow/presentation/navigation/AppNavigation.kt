package pe.edu.upc.parkingnow.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pe.edu.upc.parkingnow.presentation.view.LoginScreen
import pe.edu.upc.parkingnow.presentation.view.WelcomeScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Welcome.route
    ) {
        composable(Routes.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(Routes.Login.route) {
            LoginScreen(navController = navController)
        }
    }
}