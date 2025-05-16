package pe.upc.parkingnow.presentation.SelectRole

data class SelectRoleState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val userType: String = "", // "conductor" o "owner", por ejemplo
    val name: String = "",
    val ruc: String = "",              // Solo si es dueño
    val parkingName: String = "",      // Solo si es dueño
    val parkingAddress: String = "",   // Solo si es dueño
    val plate: String = "",            // Solo si es conductor
    val dni: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegistered: Boolean = false
)