package pe.edu.upc.parkingnow.presentation.navigation

object Routes {
    val Welcome = Screen("welcome")
    val Login = Screen("login")
    // Agrega los demás routes luego (Dashboard, Register, etc.)

    data class Screen(val route: String)
}