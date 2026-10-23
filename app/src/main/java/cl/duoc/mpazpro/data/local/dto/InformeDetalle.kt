package cl.duoc.mpazpro.data.local.dto

import cl.duoc.mpazpro.data.local.enums.Asignatura
import cl.duoc.mpazpro.data.local.enums.Clasificacion
import cl.duoc.mpazpro.data.local.enums.EstadoInforme

data class InformeDetalle(
    val id: Int,
    val alumno: String,
    val asignatura: Asignatura,
    val unidad: String,
    val codigoOa: String,
    val correctas: Int,
    val incorrectas: Int,
    val porcentaje: Int,
    val clasificacion: Clasificacion,
    val fecha: String,
    val estado: EstadoInforme,
    val docenteResponsable: String?
)