package pe.edu.upc.parkingnow.presentation.navigation

object Routes {
    val Welcome = Screen("welcome")
    val Login = Screen("login")
    val Register = Screen("register")
    val ForgotPassword = Screen("forgot_password")
    val ChangePassword = Screen("change_password")

    data class Screen(val route: String)
}