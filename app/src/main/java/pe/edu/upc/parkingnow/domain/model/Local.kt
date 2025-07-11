package pe.edu.upc.parkingnow.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioLocal(
    val id: Int,
    val nombre: String?,
    val email: String?
)

@Serializable
data class Local(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val telefono: String?,
    val plazas: Int,
    val rating: String?,
    val precio_por_hora: String?,
    val latitud: String?,
    val longitud: String?,
    val espacios_ocupados: Int?,
    val espacios_disponibles: Int?,
    val tiene_camaras: Boolean?,
    val seguridad_24h: Boolean?,
    val techado: Boolean?,
    val descripcion: String?,
    val estado: String?,
    val hora_apertura: String?,
    val hora_cierre: String?,
    val fecha_creacion: String?,
    val fecha_actualizacion: String?,
    val porcentaje_disponibilidad: Int?,
    val esta_abierto: Boolean?,
    val caracteristicas: List<String>?,
    val usuario: UsuarioLocal?
)
