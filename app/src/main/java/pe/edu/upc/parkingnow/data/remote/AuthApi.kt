package pe.edu.upc.parkingnow.data.remote

import pe.edu.upc.parkingnow.data.model.UsuarioRequest
import pe.edu.upc.parkingnow.data.model.RegisterResponse
import pe.edu.upc.parkingnow.data.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    /**
     * Crea un nuevo usuario en el sistema.
     * @param usuario datos del usuario a registrar
     * @return RegisterResponse con la información del usuario creado y el token
     */
    @POST("auth/register")
    suspend fun crearUsuario(@Body usuario: UsuarioRequest): RegisterResponse

    /**
     * Inicia sesión de usuario y retorna token y datos del usuario
     */
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): RegisterResponse
}
