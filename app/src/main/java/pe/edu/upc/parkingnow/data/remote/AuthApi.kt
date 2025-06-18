package pe.edu.upc.parkingnow.data.remote

import pe.edu.upc.parkingnow.data.model.UsuarioRequest
import pe.edu.upc.parkingnow.data.model.UsuarioResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    /**
     * Crea un nuevo usuario en el sistema.
     * @param usuario datos del usuario a registrar
     * @return UsuarioResponse con la información del usuario creado
     */
    @POST("usuario")
    suspend fun crearUsuario(@Body usuario: UsuarioRequest): UsuarioResponse
}