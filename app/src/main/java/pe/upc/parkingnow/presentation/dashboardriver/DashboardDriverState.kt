package pe.upc.parkingnow.presentation.dashboarddriver

data class DashboardDriverState(
    val driverName: String = "John Smith",
    val profileImage: Int? = null, // Puedes usar R.drawable.conductor si tienes la imagen
    val favoritePlaces: List<String> = emptyList(), // Ej: ["Estacionamiento A"]
    val weeklyOffers: List<String> = emptyList(), // Ej: ["Oferta 1", "Oferta 2"]
    val nextReservation: String = "", // Ejemplo: "Reserva de hoy a las 15:00"
    val mapUrl: String = "", // Link o identificador para mostrar el mapa (Mock por ahora)
    val isLoading: Boolean = false,
    val error: String? = null
)