package pe.upc.parkingnow.presentation.home

data class HomeState(
    val userName: String = "",
    val greeting: String = "¡Bienvenido!",
    val loading: Boolean = false,
    val error: String? = null
)