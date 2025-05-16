package pe.upc.parkingnow.presentation.dashboardowner

data class DashboardOwnerState(
    val ownerName: String = "John Smith",
    val profileImage: Int? = null, // Puedes usar R.drawable.duenho si tienes la imagen
    val todayReservations: List<String> = emptyList(), // Ej: ["Reserva 1", "Reserva 2"]
    val parkingSpaces: String = "", // Lista de espacios, separados por coma
    val isLoading: Boolean = false,
    val error: String? = null
)