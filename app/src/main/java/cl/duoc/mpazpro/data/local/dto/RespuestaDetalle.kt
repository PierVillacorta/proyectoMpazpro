package cl.duoc.mpazpro.data.local.dto

import androidx.room.Embedded
import cl.duoc.mpazpro.data.local.entity.Pregunta
import cl.duoc.mpazpro.data.local.enums.*

data class RespuestaDetalle(
    @Embedded val pregunta: Pregunta,
    val opcionElegida: Opcion,
    val esCorrecta: Boolean
)