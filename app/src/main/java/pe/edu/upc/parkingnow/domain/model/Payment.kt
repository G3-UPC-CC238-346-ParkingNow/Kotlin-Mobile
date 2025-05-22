package pe.edu.upc.parkingnow.domain.model

data class Payment(
    val id: Int,
    val userId: Int,
    val amount: Double,
    val method: String,
    val date: String,
    val bookingId: Int
)