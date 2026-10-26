package cl.duoc.mpazpro.data.local.enums

enum class Rol { UTP, DOCENTE, ESTUDIANTE }

enum class Asignatura(val nombre: String) {
    LENGUAJE("Lenguaje y Comunicación"),
    MATEMATICA("Matemática"),
    CIENCIAS("Ciencias Naturales"),
    HISTORIA("Historia, Geografía y Ciencias Sociales")
}

enum class EstadoContenido(val etiqueta: String) {
    BORRADOR("Borrador"),
    EN_REVISION("En revisión"),
    AUTORIZADO("Autorizado"),
    PUBLICADO("Publicado")
}

enum class Dificultad { FACIL, MEDIA, DIFICIL }

enum class Opcion { A, B, C, D }


enum class OrigenMaterial(val etiqueta: String) {
    PROPIO("Propio"),
    PUBLICO("Público"),
    AUTORIZADO("Autorizado")
}

enum class Clasificacion(val etiqueta: String) {
    FORTALEZA("Fortaleza"),
    EN_DESARROLLO("En desarrollo"),
    NECESITA_REFUERZO("Necesita refuerzo")
}

enum class EstadoInforme(val etiqueta: String) {
    GENERADO("Generado"),
    PENDIENTE_VALIDACION("Pendiente de validación"),
    VALIDADO("Validado")

}
    enum class TipoActividad(val etiqueta: String, val emoji: String) {
    ACTIVIDAD("Actividad", "📝"),
    DESAFIO("Desafío", "🚀")
}