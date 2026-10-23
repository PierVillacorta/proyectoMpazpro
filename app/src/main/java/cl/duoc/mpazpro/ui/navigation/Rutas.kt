package cl.duoc.mpazpro.ui.navigation

import android.net.Uri
import cl.duoc.mpazpro.data.local.enums.Asignatura

object Rutas {
    const val LOGIN = "login"
    const val ESTUDIANTE = "estudiante/{usuarioId}"
    const val DOCENTE = "docente/{usuarioId}"
    const val UTP = "utp/{usuarioId}"
    const val UNIDAD = "unidad/{usuarioId}/{asignatura}/{unidad}"
    const val PRUEBA = "prueba/{usuarioId}/{asignatura}/{unidad}"
    const val RESULTADO = "resultado/{usuarioId}/{asignatura}/{unidad}/{intento}"

    fun estudiante(id: Int) = "estudiante/$id"
    fun docente(id: Int) = "docente/$id"
    fun utp(id: Int) = "utp/$id"

    fun unidad(usuarioId: Int, asignatura: Asignatura, unidad: String) =
        "unidad/$usuarioId/${asignatura.name}/${Uri.encode(unidad)}"

    fun prueba(usuarioId: Int, asignatura: Asignatura, unidad: String) =
        "prueba/$usuarioId/${asignatura.name}/${Uri.encode(unidad)}"

    fun resultado(usuarioId: Int, asignatura: Asignatura, unidad: String, intento: Int) =
        "resultado/$usuarioId/${asignatura.name}/${Uri.encode(unidad)}/$intento"
}