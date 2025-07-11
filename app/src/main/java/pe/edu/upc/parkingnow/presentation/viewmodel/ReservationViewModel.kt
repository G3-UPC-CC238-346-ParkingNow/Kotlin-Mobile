package pe.edu.upc.parkingnow.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pe.edu.upc.parkingnow.data.model.ReservaRequest
import pe.edu.upc.parkingnow.data.model.ReservaResponse
import pe.edu.upc.parkingnow.data.model.ReservaActivaResponse
import pe.edu.upc.parkingnow.data.repository.ReservaRepositoryImpl
import android.util.Log

class ReservationViewModel : ViewModel() {
    private val repository = ReservaRepositoryImpl()

    private val _reservationSuccess = MutableStateFlow<ReservaResponse?>(null)
    val reservationSuccess: StateFlow<ReservaResponse?> = _reservationSuccess

    private val _reservationError = MutableStateFlow<String?>(null)
    val reservationError: StateFlow<String?> = _reservationError

    // Estados para las reservas activas
    private val _reservasActivas = MutableStateFlow<List<ReservaActivaResponse>>(emptyList())
    val reservasActivas: StateFlow<List<ReservaActivaResponse>> = _reservasActivas

    private val _isLoadingReservas = MutableStateFlow(false)
    val isLoadingReservas: StateFlow<Boolean> = _isLoadingReservas

    private val _reservasError = MutableStateFlow<String?>(null)
    val reservasError: StateFlow<String?> = _reservasError

    fun crearReserva(
        token: String,
        idLocal: Int,
        fhInicio: String,
        duracionHoras: Double,
        tipoVehiculo: String,
        placaVehiculo: String
    ) {
        Log.d("ReservationViewModel", "Iniciando creación de reserva")
        Log.d("ReservationViewModel", "Token recibido: ${if(token.isNotEmpty()) "SÍ" else "NO"}")
        Log.d("ReservationViewModel", "ID Local: $idLocal")
        Log.d("ReservationViewModel", "Fecha inicio: $fhInicio")

        val request = ReservaRequest(
            id_local = idLocal,
            fh_inicio = fhInicio,
            duracion_horas = duracionHoras,
            tipo_vehiculo = tipoVehiculo,
            placa_vehiculo = placaVehiculo,
            notas = null
        )

        Log.d("ReservationViewModel", "Request creado: $request")

        viewModelScope.launch {
            try {
                val response: ReservaResponse? = repository.crearReserva(token, request)
                if (response != null) {
                    Log.d("ReservationViewModel", "Reserva exitosa: ${response.codigo_reserva}")
                    _reservationSuccess.value = response
                    // Actualizar lista de reservas activas después de crear una nueva
                    obtenerReservasActivas(token)
                } else {
                    Log.e("ReservationViewModel", "Respuesta nula del repositorio")
                    _reservationError.value = "Error al crear la reserva"
                }
            } catch (e: Exception) {
                Log.e("ReservationViewModel", "Excepción en crearReserva: ${e.message}", e)
                _reservationError.value = e.message
            }
        }
    }

    fun obtenerReservasActivas(token: String) {
        Log.d("ReservationViewModel", "Obteniendo reservas activas")

        if (token.isBlank()) {
            Log.e("ReservationViewModel", "Token vacío, no se puede obtener reservas")
            _reservasError.value = "No hay sesión activa"
            _isLoadingReservas.value = false
            return
        }

        _isLoadingReservas.value = true
        _reservasError.value = null

        viewModelScope.launch {
            try {
                Log.d("ReservationViewModel", "Llamando al repositorio...")
                val response = repository.obtenerReservasActivas(token)
                Log.d("ReservationViewModel", "Respuesta del repositorio: $response")

                if (response != null) {
                    Log.d("ReservationViewModel", "Reservas obtenidas exitosamente: ${response.size}")
                    _reservasActivas.value = response
                    _reservasError.value = null
                } else {
                    Log.e("ReservationViewModel", "Respuesta nula del repositorio")
                    _reservasError.value = "No se pudieron cargar las reservas. Verifica tu conexión."
                    _reservasActivas.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("ReservationViewModel", "Excepción en obtenerReservasActivas: ${e.message}", e)
                _reservasError.value = "Error de conexión. Verifica tu internet."
                _reservasActivas.value = emptyList()
            } finally {
                _isLoadingReservas.value = false
            }
        }
    }

    // Limpiar estados
    fun clearReservationStates() {
        _reservationSuccess.value = null
        _reservationError.value = null
    }

    fun clearReservasStates() {
        _reservasError.value = null
    }
}
