package pe.edu.upc.parkingnow.data.repository

import pe.edu.upc.parkingnow.data.model.UsuarioRequest
import pe.edu.upc.parkingnow.data.model.RegisterResponse
import pe.edu.upc.parkingnow.data.model.LoginRequest
import pe.edu.upc.parkingnow.data.remote.AuthApi
import pe.edu.upc.parkingnow.data.remote.RetrofitInstance
import retrofit2.Response

class AuthRepositoryImpl(
    private val api: AuthApi = RetrofitInstance.retrofit.create(AuthApi::class.java)
) {
    suspend fun registrarUsuario(usuarioRequest: UsuarioRequest): Result<RegisterResponse?> {
        return try {
            val response = api.crearUsuario(usuarioRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(request: LoginRequest): Result<RegisterResponse?> {
        return try {
            val response = api.login(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

