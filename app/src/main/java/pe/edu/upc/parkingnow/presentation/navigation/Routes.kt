package pe.edu.upc.parkingnow.presentation.navigation

object Routes {
    val Welcome = Screen("welcome")
    val Login = Screen("login")
    val Register = Screen("register")
    val ForgotPassword = Screen("forgot_password")
    val ChangePassword = Screen("change_password")
    val Dashboard = Screen("dashboard")
    val Bookings = Screen("bookings")
    val Ticket = Screen("ticket")
    val Success = Screen("success")
    val Support = Screen("support")
    val Tracking = Screen("tracking")
    val Settings = Screen("settings")
    val Notifications = Screen("notifications")
    val Payment = Screen("payment")

    data class Screen(val route: String)
}