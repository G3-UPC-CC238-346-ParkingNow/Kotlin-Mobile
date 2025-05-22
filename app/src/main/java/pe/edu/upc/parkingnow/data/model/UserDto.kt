package pe.edu.upc.parkingnow.data.model

data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String,
    val role: String
)