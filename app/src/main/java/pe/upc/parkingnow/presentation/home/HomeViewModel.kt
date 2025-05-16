package pe.upc.parkingnow.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var state = HomeState()
        private set

    init {
        // Simulación de carga de usuario
        viewModelScope.launch {
            state = state.copy(loading = true)
            delay(1000) // Simula carga
            state = state.copy(
                loading = false,
                userName = "Diego",
                greeting = "¡Bienvenido de nuevo!"
            )
        }
    }
}