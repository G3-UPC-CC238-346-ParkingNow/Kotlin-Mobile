package pe.edu.upc.parkingnow.domain.model

data class Notification(
    val id: Int,
    val userId: Int,
    val message: String,
    val date: String,
    val read: Boolean
)