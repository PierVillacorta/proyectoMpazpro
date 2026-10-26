package cl.duoc.mpazpro.ui.theme

import androidx.compose.ui.graphics.Color
import cl.duoc.mpazpro.data.local.enums.Asignatura

val Asignatura.colorTema: Color
    get() = when (this) {
        Asignatura.LENGUAJE -> Color(0xFFD32F2F)    // rojo
        Asignatura.MATEMATICA -> Color(0xFF1E88E5)  // azul
        Asignatura.CIENCIAS -> Color(0xFF2E7D32)    // verde
        Asignatura.HISTORIA -> Color(0xFFFBC02D)    // amarillo
    }

// Color del texto que va sobre la tarjeta (oscuro sobre amarillo para que se lea)
val Asignatura.colorTexto: Color
    get() = when (this) {
        Asignatura.HISTORIA -> Color(0xFF1B1B1F)
        else -> Color.White
    }

val Asignatura.emoji: String
    get() = when (this) {
        Asignatura.LENGUAJE -> "📚"
        Asignatura.MATEMATICA -> "🧮"
        Asignatura.CIENCIAS -> "🔬"
        Asignatura.HISTORIA -> "🌎"
    }