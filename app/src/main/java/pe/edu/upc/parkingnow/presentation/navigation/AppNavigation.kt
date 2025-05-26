package pe.edu.upc.parkingnow.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pe.edu.upc.parkingnow.presentation.view.LoginScreen
import pe.edu.upc.parkingnow.presentation.view.WelcomeScreen
import pe.edu.upc.parkingnow.presentation.view.RegisterScreen
import pe.edu.upc.parkingnow.presentation.view.ForgotPasswordScreen
import pe.edu.upc.parkingnow.presentation.view.ChangePasswordScreen
import pe.edu.upc.parkingnow.presentation.view.DashboardScreen
import pe.edu.upc.parkingnow.presentation.view.BookingsScreen
import pe.edu.upc.parkingnow.presentation.view.TicketScreen
import pe.edu.upc.parkingnow.presentation.view.SuccessScreen
import pe.edu.upc.parkingnow.presentation.view.PaymentScreen
import pe.edu.upc.parkingnow.presentation.view.SupportScreen
import pe.edu.upc.parkingnow.presentation.view.TrackingScreen
import pe.edu.upc.parkingnow.presentation.view.SettingsScreen
import pe.edu.upc.parkingnow.presentation.view.NotificationsScreen

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
        composable(Routes.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Routes.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }
        composable(Routes.ChangePassword.route) {
            ChangePasswordScreen(navController = navController)
        }
        composable("dashboard/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Usuario"
            DashboardScreen(navController = navController, username = username)
        }
        composable("bookings") {
            BookingsScreen(navController = navController)
        }
        composable("ticket") {
            TicketScreen(navController = navController)
        }
        composable("success") {
            SuccessScreen(navController = navController)
        }
        composable("payment") {
            PaymentScreen(navController = navController)
        }
        composable("support") {
            SupportScreen(navController = navController)
        }
        composable("tracking") {
            TrackingScreen(navController = navController)
        }
        composable("settings") {
            SettingsScreen(navController = navController)
        }
        composable("notifications") {
            NotificationsScreen(navController = navController)
        }
    }
}