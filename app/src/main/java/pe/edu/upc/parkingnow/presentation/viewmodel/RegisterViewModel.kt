package pe.edu.upc.parkingnow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import pe.edu.upc.parkingnow.data.model.UsuarioRequest
import pe.edu.upc.parkingnow.data.model.UsuarioResponse
import pe.edu.upc.parkingnow.data.repository.AuthRepositoryImpl

class RegisterViewModel : ViewModel() {
    private val repository = AuthRepositoryImpl()

    private val _registerSuccess = mutableStateOf<UsuarioResponse?>(null)
    val registerSuccess: State<UsuarioResponse?> = _registerSuccess

    private val _registerError = mutableStateOf<String?>(null)
    val registerError: State<String?> = _registerError

    fun registrarUsuario(request: UsuarioRequest) {
        viewModelScope.launch {
            repository.registrarUsuario(
                usuarioRequest = request,
                onSuccess = { response -> _registerSuccess.value = response },
                onError = { error -> _registerError.value = error.message }
            )
        }
    }
}