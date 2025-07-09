package pe.edu.upc.parkingnow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import pe.edu.upc.parkingnow.data.model.LoginRequest
import pe.edu.upc.parkingnow.data.model.RegisterResponse
import pe.edu.upc.parkingnow.data.repository.AuthRepositoryImpl

class LoginViewModel : ViewModel() {
    private val repository = AuthRepositoryImpl()

    private val _loginSuccess = mutableStateOf<RegisterResponse?>(null)
    val loginSuccess: State<RegisterResponse?> = _loginSuccess

    private val _loginError = mutableStateOf<String?>(null)
    val loginError: State<String?> = _loginError

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            val result = repository.login(request)
            result
                .onSuccess { response -> _loginSuccess.value = response }
                .onFailure { error -> _loginError.value = error.message }
        }
    }
}

