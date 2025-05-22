package pe.edu.upc.parkingnow.data.model

data class BookingDto(
    val id: Int,
    val userId: Int,
    val parkingSlotId: Int,
    val startTime: String,
    val endTime: String,
    val status: String
)