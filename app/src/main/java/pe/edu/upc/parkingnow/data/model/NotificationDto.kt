package pe.edu.upc.parkingnow.data.model

data class NotificationDto(
    val id: Int,
    val userId: Int,
    val message: String,
    val date: String,
    val read: Boolean
)