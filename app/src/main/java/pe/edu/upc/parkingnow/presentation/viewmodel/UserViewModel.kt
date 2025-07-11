package pe.edu.upc.parkingnow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserViewModel : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _userToken = MutableStateFlow("")
    val userToken: StateFlow<String> = _userToken

    private val _userPlaca = MutableStateFlow("")
    val userPlaca: StateFlow<String> = _userPlaca

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail

    fun setUsername(name: String) {
        _username.value = name
    }

    fun setUserToken(token: String) {
        _userToken.value = token
    }

    fun setUserPlaca(placa: String) {
        _userPlaca.value = placa
    }

    fun setUserEmail(email: String) {
        _userEmail.value = email
    }

    // Función para cargar datos del usuario desde SharedPreferences
    fun loadUserData(token: String, username: String, email: String, placa: String = "") {
        _userToken.value = token
        _username.value = username
        _userEmail.value = email
        _userPlaca.value = placa
    }

    // Función para limpiar datos del usuario al cerrar sesión
    fun clearUserData() {
        _userToken.value = ""
        _username.value = ""
        _userEmail.value = ""
        _userPlaca.value = ""
    }
}
