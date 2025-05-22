package pe.edu.upc.parkingnow.data.model

data class PaymentDto(
    val id: Int,
    val userId: Int,
    val amount: Double,
    val method: String,
    val date: String,
    val bookingId: Int
)