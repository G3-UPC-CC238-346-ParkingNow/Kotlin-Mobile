package pe.edu.upc.parkingnow.data.model

data class UsuarioResponse(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val placa: String,
    val dni: String,
    val ruc: String?,
    val tipoUsuario: String,
    val locales: List<Any>
)
