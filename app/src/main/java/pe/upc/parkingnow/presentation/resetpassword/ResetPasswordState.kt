package pe.upc.parkingnow.presentation.resetpassword

data class ResetPasswordState(
    val email: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isResetSuccessful: Boolean = false
)