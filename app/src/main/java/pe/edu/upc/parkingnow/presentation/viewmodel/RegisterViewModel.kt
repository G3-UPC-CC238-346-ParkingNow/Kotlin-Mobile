package pe.edu.upc.parkingnow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import pe.edu.upc.parkingnow.data.model.UsuarioRequest
import pe.edu.upc.parkingnow.data.model.RegisterResponse
import pe.edu.upc.parkingnow.data.repository.AuthRepositoryImpl

class RegisterViewModel : ViewModel() {
    private val repository = AuthRepositoryImpl()

    private val _registerSuccess = mutableStateOf<RegisterResponse?>(null)
    val registerSuccess: State<RegisterResponse?> = _registerSuccess

    private val _registerError = mutableStateOf<String?>(null)
    val registerError: State<String?> = _registerError

    fun registrarUsuario(request: UsuarioRequest) {
        viewModelScope.launch {
            val result = repository.registrarUsuario(request)
            result
                .onSuccess { response -> _registerSuccess.value = response }
                .onFailure { error -> _registerError.value = error.message }
        }
    }
}

