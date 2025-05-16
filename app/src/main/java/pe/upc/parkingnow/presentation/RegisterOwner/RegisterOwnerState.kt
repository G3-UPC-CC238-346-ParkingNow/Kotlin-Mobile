package pe.upc.parkingnow.presentation.login.RegisterOwner

data class RegisterOwnerState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val ruc: String = "",
    val businessName: String = "",
    val address: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val errorMessage: String? = null
)