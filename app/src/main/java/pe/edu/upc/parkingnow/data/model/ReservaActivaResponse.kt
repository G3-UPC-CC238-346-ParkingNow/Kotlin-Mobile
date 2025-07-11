package pe.edu.upc.parkingnow.data.model

import com.google.gson.annotations.SerializedName

data class ReservaActivaResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("fh_inicio")
    val fechaInicio: String,

    @SerializedName("fh_fin")
    val fechaFin: String,

    @SerializedName("duracion_horas")
    val duracionHoras: String,

    @SerializedName("tipo_vehiculo")
    val tipoVehiculo: String,

    @SerializedName("placa_vehiculo")
    val placaVehiculo: String,

    @SerializedName("precio_por_hora")
    val precioPorHora: String,

    @SerializedName("total_pagado")
    val precioTotal: String,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("nro_plaza")
    val nroPlaza: Int?,

    @SerializedName("notas")
    val notas: String?,

    @SerializedName("codigo_reserva")
    val codigoReserva: String,

    @SerializedName("fecha_creacion")
    val fechaCreacion: String,

    @SerializedName("fecha_actualizacion")
    val fechaActualizacion: String,

    @SerializedName("esta_activa")
    val estaActiva: Boolean,

    @SerializedName("ha_expirado")
    val haExpirado: Boolean,

    @SerializedName("tiempo_restante_minutos")
    val tiempoRestanteMinutos: Int,

    @SerializedName("tipo_vehiculo_texto")
    val tipoVehiculoTexto: String,

    @SerializedName("usuario")
    val usuario: UsuarioReservaActiva,

    @SerializedName("local")
    val local: LocalReservaActiva
) {
    // Propiedades computadas para compatibilidad con el UI existente
    val nombreLocal: String get() = local.nombre
    val direccionLocal: String get() = local.direccion
    val precioTotalDouble: Double get() = precioTotal.toDoubleOrNull() ?: 0.0
    val duracionHorasDouble: Double get() = duracionHoras.toDoubleOrNull() ?: 0.0
}

data class UsuarioReservaActiva(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("placa")
    val placa: String?
)

data class LocalReservaActiva(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("direccion")
    val direccion: String,

    @SerializedName("telefono")
    val telefono: String,

    @SerializedName("latitud")
    val latitud: Double,

    @SerializedName("longitud")
    val longitud: Double
)

// El backend retorna directamente un array, no un objeto con propiedad "reservas"
typealias ReservasActivasResponse = List<ReservaActivaResponse>

