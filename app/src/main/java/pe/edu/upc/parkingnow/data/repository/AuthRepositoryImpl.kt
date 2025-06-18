package pe.edu.upc.parkingnow.data.repository

import pe.edu.upc.parkingnow.data.model.UsuarioRequest
import pe.edu.upc.parkingnow.data.model.UsuarioResponse
import pe.edu.upc.parkingnow.data.remote.AuthApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepositoryImpl {

    private val api: AuthApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(AuthApi::class.java)
    }

    fun registrarUsuario(
        usuarioRequest: UsuarioRequest,
        onSuccess: (UsuarioResponse?) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = api.crearUsuario(usuarioRequest)
        call.enqueue(object : retrofit2.Callback<UsuarioResponse> {
            override fun onResponse(
                call: retrofit2.Call<UsuarioResponse>,
                response: retrofit2.Response<UsuarioResponse>
            ) {
                if (response.isSuccessful) {
                    onSuccess(response.body())
                } else {
                    onError(Exception("Error en la respuesta"))
                }
            }

            override fun onFailure(call: retrofit2.Call<UsuarioResponse>, t: Throwable) {
                onError(t)
            }
        })
    }
}