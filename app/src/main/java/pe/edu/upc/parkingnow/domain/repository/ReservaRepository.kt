package pe.edu.upc.parkingnow.domain.repository

import pe.edu.upc.parkingnow.data.model.ReservaRequest
import pe.edu.upc.parkingnow.data.model.ReservaResponse

interface ReservaRepository {
    suspend fun crearReserva(token: String, reserva: ReservaRequest): ReservaResponse?
}