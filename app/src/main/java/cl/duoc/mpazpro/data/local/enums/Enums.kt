package cl.duoc.mpazpro.data.local.enums

enum class Rol { UTP, DOCENTE, ESTUDIANTE }

enum class Asignatura(val nombre: String) {
    LENGUAJE("Lenguaje y Comunicación"),
    MATEMATICA("Matemática"),
    CIENCIAS("Ciencias Naturales"),
    HISTORIA("Historia, Geografía y Ciencias Sociales")
}

enum class EstadoContenido { BORRADOR, EN_REVISION, AUTORIZADO, PUBLICADO }

enum class Dificultad { FACIL, MEDIA, DIFICIL }

enum class Opcion { A, B, C, D }

enum class OrigenMaterial { PROPIO, PUBLICO, AUTORIZADO }

enum class Clasificacion { FORTALEZA, EN_DESARROLLO, NECESITA_REFUERZO }

enum class EstadoInforme { GENERADO, PENDIENTE_VALIDACION, VALIDADO }