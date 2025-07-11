package pe.edu.upc.parkingnow.data.remote

import pe.edu.upc.parkingnow.data.model.ReservaRequest
import pe.edu.upc.parkingnow.data.model.ReservaResponse
import pe.edu.upc.parkingnow.data.model.ReservasActivasResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET

interface ReservaApiService {
    @POST("reserva")
    suspend fun crearReserva(
        @Header("Authorization") token: String,
        @Body reserva: ReservaRequest
    ): ReservaResponse

    @GET("reserva/activas")
    suspend fun obtenerReservasActivas(
        @Header("Authorization") token: String
    ): ReservasActivasResponse
}
