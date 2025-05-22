package pe.edu.upc.parkingnow.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val role: String
)