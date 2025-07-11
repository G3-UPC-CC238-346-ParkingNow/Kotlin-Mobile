package pe.edu.upc.parkingnow.data.model

data class ReservaResponse(
    val id: Int,
    val fh_inicio: String,
    val fh_fin: String,
    val duracion_horas: Double,
    val tipo_vehiculo: String,
    val placa_vehiculo: String,
    val precio_por_hora: String,
    val total_pagado: Double,
    val estado: String,
    val nro_plaza: Int?,
    val notas: String?,
    val codigo_reserva: String,
    val fecha_creacion: String,
    val fecha_actualizacion: String,
    val esta_activa: Boolean,
    val ha_expirado: Boolean,
    val tiempo_restante_minutos: Int,
    val tipo_vehiculo_texto: String,
    val usuario: UsuarioReserva,
    val local: LocalReserva
)

data class UsuarioReserva(
    val id: Int,
    val email: String
)

data class LocalReserva(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val telefono: String,
    val latitud: Double,
    val longitud: Double
)