package pe.edu.upc.parkingnow.data.model

data class UserRegisterResponse(
    val id: Int,
    val name: String,
    val email: String,
    val tipoUsuario: String
)

data class RegisterResponse(
    val access_token: String,
    val user: UserRegisterResponse
)

