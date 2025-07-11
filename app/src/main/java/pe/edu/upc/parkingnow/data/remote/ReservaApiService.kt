package pe.edu.upc.parkingnow.data.remote

import pe.edu.upc.parkingnow.data.model.ReservaRequest
import pe.edu.upc.parkingnow.data.model.ReservaResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ReservaApiService {
    @POST("reserva")
    suspend fun crearReserva(
        @Header("Authorization") token: String,
        @Body reserva: ReservaRequest
    ): ReservaResponse
}