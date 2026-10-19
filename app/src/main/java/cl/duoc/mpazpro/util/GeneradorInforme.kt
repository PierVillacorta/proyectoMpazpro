package cl.duoc.mpazpro.util

import cl.duoc.mpazpro.data.local.dto.RendimientoOa
import cl.duoc.mpazpro.data.local.entity.Informe
import cl.duoc.mpazpro.data.local.enums.*

object GeneradorInforme {

    fun clasificar(porcentaje: Int): Clasificacion = when {
        porcentaje >= 70 -> Clasificacion.FORTALEZA
        porcentaje < 50 -> Clasificacion.NECESITA_REFUERZO
        else -> Clasificacion.EN_DESARROLLO
    }

    fun generar(usuarioId: Int, rendimientos: List<RendimientoOa>, fecha: String): List<Informe> =
        rendimientos.map { r ->
            val porcentaje = if (r.total == 0) 0 else r.correctas * 100 / r.total
            Informe(
                usuarioId = usuarioId,
                oaId = r.oaId,
                correctas = r.correctas,
                incorrectas = r.total - r.correctas,
                porcentaje = porcentaje,
                clasificacion = clasificar(porcentaje),
                fecha = fecha,
                estado = EstadoInforme.PENDIENTE_VALIDACION
            )
        }
}