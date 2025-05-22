package pe.edu.upc.parkingnow.domain.model

data class SupportMessage(
    val id: Int,
    val userId: Int,
    val subject: String,
    val message: String,
    val date: String
)