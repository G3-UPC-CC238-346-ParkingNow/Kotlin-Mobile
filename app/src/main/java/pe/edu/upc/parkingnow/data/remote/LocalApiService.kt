package pe.edu.upc.parkingnow.data.remote

import pe.edu.upc.parkingnow.domain.model.Local
import retrofit2.http.GET
import retrofit2.http.Query

interface LocalApiService {
    @GET("local")
    suspend fun getLocales(): List<Local>

    @GET("local/cercanos")
    suspend fun getLocalesCercanos(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("radio") radio: Int = 10000
    ): List<Local>
}

