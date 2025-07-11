package pe.edu.upc.parkingnow.data.repository

import pe.edu.upc.parkingnow.domain.model.Local
import pe.edu.upc.parkingnow.domain.repository.LocalRepository
import pe.edu.upc.parkingnow.data.remote.RetrofitInstance
import pe.edu.upc.parkingnow.data.remote.LocalApiService

class LocalRepositoryImpl : LocalRepository {
    private val apiService = RetrofitInstance.retrofit.create(LocalApiService::class.java)
    override suspend fun getLocales(): List<Local> {
        return try {
            apiService.getLocales()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getLocalesCercanos(lat: Double, lng: Double, radio: Int = 10000): List<Local> {
        return try {
            apiService.getLocalesCercanos(lat, lng, radio)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
