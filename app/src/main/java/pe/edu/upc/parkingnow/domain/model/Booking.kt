package pe.edu.upc.parkingnow.domain.model

data class Booking(
    val id: Int,
    val userId: Int,
    val parkingSlotId: Int,
    val startTime: String,
    val endTime: String,
    val status: String
)