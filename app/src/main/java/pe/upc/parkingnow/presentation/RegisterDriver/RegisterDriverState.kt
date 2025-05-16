package pe.upc.parkingnow.presentation.login.RegisterDriver

data class RegisterDriverState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val licensePlate: String = "",
    val dni: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val errorMessage: String? = null
)