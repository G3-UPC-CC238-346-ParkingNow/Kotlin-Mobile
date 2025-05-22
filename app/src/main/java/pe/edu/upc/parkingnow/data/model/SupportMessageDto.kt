package pe.edu.upc.parkingnow.data.model

data class SupportMessageDto(
    val id: Int,
    val userId: Int,
    val subject: String,
    val message: String,
    val date: String
)