package pe.upc.parkingnow.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pe.upc.parkingnow.presentation.home.HomeScreen
import pe.upc.parkingnow.presentation.login.LoginScreen
import pe.upc.parkingnow.presentation.forgotpassword.ForgotPasswordScreen
import pe.upc.parkingnow.presentation.resetpassword.ResetPasswordScreen
import pe.upc.parkingnow.presentation.SelectRole.SelectRoleScreen
import pe.upc.parkingnow.presentation.SelectRole.Role
import pe.upc.parkingnow.presentation.login.RegisterDriver.RegisterDriverScreen
import pe.upc.parkingnow.presentation.login.RegisterOwner.RegisterOwnerScreen
import pe.upc.parkingnow.presentation.dashboarddriver.DashboardDriverScreen
import pe.upc.parkingnow.presentation.dashboardowner.DashboardOwnerScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Home
        composable("home") {
            HomeScreen(
                onStartClick = { navController.navigate("login") },
                onRegisterClick = { navController.navigate("selectRole") }
            )
        }
        // Login
        composable("login") {
            LoginScreen(
                onLoginSuccess = { /* navega donde corresponda después de login */ },
                onForgotPassword = { navController.navigate("forgot") },
                onRegisterClick = { navController.navigate("selectRole") }
            )
        }
        // Forgot Password
        composable("forgot") {
            ForgotPasswordScreen(
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate("reset") }
            )
        }
        // Reset Password
        composable("reset") {
            ResetPasswordScreen(
                onBack = { navController.popBackStack() },
                onChangePassword = { /* navega a home o muestra mensaje de éxito */ }
            )
        }
        // Select Role
        composable("selectRole") {
            SelectRoleScreen(
                onRoleSelected = { role ->
                    when (role) {
                        Role.DRIVER -> navController.navigate("registerDriver")
                        Role.OWNER -> navController.navigate("registerOwner")
                    }
                }
            )
        }
        // Register Driver
        composable("registerDriver") {
            RegisterDriverScreen(
                onBack = { navController.popBackStack() },
                onRegister = { /* lógica de registro */ },
                onRegisterSuccess = { navController.navigate("dashboardDriver") }
            )
        }
        // Register Owner
        composable("registerOwner") {
            RegisterOwnerScreen(
                onBack = { navController.popBackStack() },
                onRegister = { /* lógica de registro */ },
                onRegisterSuccess = { navController.navigate("dashboardOwner") }
            )
        }
        // Dashboard Driver
        composable("dashboardDriver") {
            DashboardDriverScreen()
        }
        // Dashboard Owner
        composable("dashboardOwner") {
            DashboardOwnerScreen()
        }
    }
}