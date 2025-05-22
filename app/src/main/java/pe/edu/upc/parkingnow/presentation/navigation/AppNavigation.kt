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
    }
}