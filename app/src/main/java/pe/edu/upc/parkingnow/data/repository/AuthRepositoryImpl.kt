package pe.edu.upc.parkingnow.data.repository

import pe.edu.upc.parkingnow.data.model.UsuarioRequest
import pe.edu.upc.parkingnow.data.model.UsuarioResponse
import pe.edu.upc.parkingnow.data.remote.AuthApi
import pe.edu.upc.parkingnow.data.remote.RetrofitInstance
import retrofit2.Response

class AuthRepositoryImpl(
    private val api: AuthApi = RetrofitInstance.retrofit.create(AuthApi::class.java)
) {
    suspend fun registrarUsuario(usuarioRequest: UsuarioRequest): Result<UsuarioResponse?> {
        return try {
            val response = api.crearUsuario(usuarioRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}