package pe.edu.upc.parkingnow.data.model

data class UsuarioRequest(
    val name: String,
    val email: String,
    val password: String,
    val placa: String,
    val dni: String,
    val ruc: String? = null
)
