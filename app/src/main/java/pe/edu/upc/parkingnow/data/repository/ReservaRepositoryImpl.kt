package pe.edu.upc.parkingnow.data.repository

import pe.edu.upc.parkingnow.data.remote.ReservaApiService
import pe.edu.upc.parkingnow.data.model.ReservaRequest
import pe.edu.upc.parkingnow.data.model.ReservaResponse
import pe.edu.upc.parkingnow.domain.repository.ReservaRepository
import pe.edu.upc.parkingnow.data.remote.RetrofitInstance
import android.util.Log

class ReservaRepositoryImpl : ReservaRepository {
    private val api = RetrofitInstance.retrofit.create(ReservaApiService::class.java)

    override suspend fun crearReserva(token: String, reserva: ReservaRequest): ReservaResponse? {
        return try {
            Log.d("ReservaRepository", "Enviando request: $reserva")
            Log.d("ReservaRepository", "Token: $token")
            Log.d("ReservaRepository", "URL: ${RetrofitInstance.retrofit.baseUrl()}")

            val response = api.crearReserva("Bearer $token", reserva)
            Log.d("ReservaRepository", "Respuesta exitosa: $response")
            response
        } catch (e: Exception) {
            Log.e("ReservaRepository", "Error al crear reserva: ${e.message}", e)
            Log.e("ReservaRepository", "Tipo de error: ${e.javaClass.simpleName}")
            null
        }
    }
}

