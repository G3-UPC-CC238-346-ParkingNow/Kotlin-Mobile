package pe.edu.upc.parkingnow.presentation.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import pe.edu.upc.parkingnow.data.model.ReservaRequest
import pe.edu.upc.parkingnow.data.model.ReservaResponse
import pe.edu.upc.parkingnow.data.repository.ReservaRepositoryImpl
import android.util.Log

class ReservationViewModel : ViewModel() {
    private val repository = ReservaRepositoryImpl()

    private val _reservationSuccess = MutableStateFlow<ReservaResponse?>(null)
    val reservationSuccess: StateFlow<ReservaResponse?> = _reservationSuccess

    private val _reservationError = MutableStateFlow<String?>(null)
    val reservationError: StateFlow<String?> = _reservationError

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
}
