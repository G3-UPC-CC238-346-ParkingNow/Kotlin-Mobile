package pe.edu.upc.parkingnow.data.model

data class ReservaRequest(
    val id_local: Int,
    val fh_inicio: String,
    val duracion_horas: Double,
    val tipo_vehiculo: String,
    val placa_vehiculo: String,
    val notas: String? = null
)