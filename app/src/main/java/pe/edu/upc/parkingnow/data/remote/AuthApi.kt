package pe.edu.upc.parkingnow.data.remote

import pe.edu.upc.parkingnow.data.model.UsuarioRequest
import pe.edu.upc.parkingnow.data.model.UsuarioResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("usuario")
    fun crearUsuario(@Body usuario: UsuarioRequest): Call<UsuarioResponse>
}