package pe.edu.upc.parkingnow.data.repository

import pe.edu.upc.parkingnow.data.remote.ReservaApiService
import pe.edu.upc.parkingnow.data.model.ReservaRequest
import pe.edu.upc.parkingnow.data.model.ReservaResponse
import pe.edu.upc.parkingnow.data.model.ReservasActivasResponse
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

    override suspend fun obtenerReservasActivas(token: String): ReservasActivasResponse? {
        return try {
            Log.d("ReservaRepository", "Obteniendo reservas activas")
            Log.d("ReservaRepository", "Token: $token")
            Log.d("ReservaRepository", "URL completa: ${RetrofitInstance.retrofit.baseUrl()}reserva/activas")

            if (token.isBlank()) {
                Log.e("ReservaRepository", "Token vacío")
                return null
            }

            val response = api.obtenerReservasActivas("Bearer $token")
            Log.d("ReservaRepository", "Reservas obtenidas: ${response.size}")
            Log.d("ReservaRepository", "Primera reserva: ${response.firstOrNull()}")
            response
        } catch (e: retrofit2.HttpException) {
            Log.e("ReservaRepository", "Error HTTP ${e.code()}: ${e.message()}")
            Log.e("ReservaRepository", "Response body: ${e.response()?.errorBody()?.string()}")
            null
        } catch (e: java.net.UnknownHostException) {
            Log.e("ReservaRepository", "Error de conexión: No se puede conectar al servidor")
            null
        } catch (e: java.net.SocketTimeoutException) {
            Log.e("ReservaRepository", "Timeout de conexión")
            null
        } catch (e: com.google.gson.JsonSyntaxException) {
            Log.e("ReservaRepository", "Error de parsing JSON: ${e.message}")
            null
        } catch (e: Exception) {
            Log.e("ReservaRepository", "Error general al obtener reservas activas: ${e.message}", e)
            Log.e("ReservaRepository", "Tipo de error: ${e.javaClass.simpleName}")
            null
        }
    }
}

