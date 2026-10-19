package cl.duoc.mpazpro.ui.navigation

object Rutas {
    const val LOGIN = "login"
    const val ESTUDIANTE = "estudiante/{usuarioId}"
    const val DOCENTE = "docente/{usuarioId}"
    const val UTP = "utp/{usuarioId}"

    fun estudiante(id: Int) = "estudiante/$id"
    fun docente(id: Int) = "docente/$id"
    fun utp(id: Int) = "utp/$id"
}