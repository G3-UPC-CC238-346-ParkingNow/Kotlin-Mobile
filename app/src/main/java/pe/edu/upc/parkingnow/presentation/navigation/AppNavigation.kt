package pe.edu.upc.parkingnow.presentation.navigation


import pe.edu.upc.parkingnow.presentation.viewmodel.UserViewModel
import pe.edu.upc.parkingnow.presentation.viewmodel.AppViewModel

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
import pe.edu.upc.parkingnow.presentation.view.PaymentScreen
import pe.edu.upc.parkingnow.presentation.view.SupportScreen
import pe.edu.upc.parkingnow.presentation.view.TrackingScreen
import pe.edu.upc.parkingnow.presentation.view.SettingsScreen
import pe.edu.upc.parkingnow.presentation.view.NotificationsScreen
import pe.edu.upc.parkingnow.presentation.view.SuccessScreen

@Composable
fun AppNavigation(navController: NavHostController, userViewModel: UserViewModel, appViewModel: AppViewModel) {
    NavHost(
        navController = navController,
        startDestination = Routes.Welcome.route
    ) {
        composable(Routes.Welcome.route) {
            WelcomeScreen(navController = navController)
        }
        composable(Routes.Login.route) {
            LoginScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(Routes.Register.route) {
            RegisterScreen(navController = navController, userViewModel = userViewModel, appViewModel = appViewModel)
        }
        composable(Routes.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(Routes.ChangePassword.route) {
            ChangePasswordScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(Routes.Dashboard.route) {
            DashboardScreen(navController = navController, userViewModel = userViewModel, appViewModel = appViewModel)
        }
        composable(Routes.Bookings.route) {
            BookingsScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(Routes.Ticket.route) {
            TicketScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(Routes.Success.route) {
            SuccessScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(Routes.Payment.route) {
            PaymentScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(Routes.Support.route) {
            SupportScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(Routes.Tracking.route) {
            TrackingScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(Routes.Settings.route) {
            SettingsScreen(navController = navController, appViewModel = appViewModel)
        }
        composable(Routes.Notifications.route) {
            NotificationsScreen(navController = navController, appViewModel = appViewModel)
        }
    }
}