package pe.edu.upc.parkingnow.data.model

data class LoginResponse(
    val token: String,
    val userId: Int,
    val role: String
)