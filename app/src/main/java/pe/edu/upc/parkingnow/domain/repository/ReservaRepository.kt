package pe.edu.upc.parkingnow.domain.repository

import pe.edu.upc.parkingnow.data.model.ReservaRequest
import pe.edu.upc.parkingnow.data.model.ReservaResponse
import pe.edu.upc.parkingnow.data.model.ReservasActivasResponse

interface ReservaRepository {
    suspend fun crearReserva(token: String, reserva: ReservaRequest): ReservaResponse?
    suspend fun obtenerReservasActivas(token: String): ReservasActivasResponse?
}

