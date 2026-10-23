package cl.duoc.mpazpro.ui.estudiante

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.entity.Pregunta
import cl.duoc.mpazpro.data.local.entity.Respuesta
import cl.duoc.mpazpro.data.local.enums.Asignatura
import cl.duoc.mpazpro.data.local.enums.Opcion
import cl.duoc.mpazpro.util.GeneradorInforme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PruebaViewModel(
    private val db: AppDatabase,
    private val usuarioId: Int,
    private val asignatura: Asignatura,
    private val unidad: String
) : ViewModel() {

    var preguntas by mutableStateOf<List<Pregunta>>(emptyList())
        private set
    var indice by mutableIntStateOf(0)
        private set
    var elegida by mutableStateOf<Opcion?>(null)
    var guardando by mutableStateOf(false)
        private set
    var bloqueada by mutableStateOf(false)
        private set

    private val respuestas = mutableMapOf<Int, Opcion>()   // preguntaId -> opción elegida

    val actual: Pregunta? get() = preguntas.getOrNull(indice)
    val esUltima: Boolean get() = indice == preguntas.lastIndex

    init {
        viewModelScope.launch {
            val usados = db.respuestaDao().intentosUsados(usuarioId, asignatura, unidad)
            val maximo = db.configPruebaDao().obtener(asignatura, unidad)?.maxIntentos ?: 1
            if (usados >= maximo) {
                bloqueada = true
            } else {
                preguntas = db.preguntaDao().porUnidadAsignada(usuarioId, asignatura, unidad)
            }
        }
    }

    fun avanzar(onTerminado: (Int) -> Unit) {
        val pregunta = actual ?: return
        val opcion = elegida ?: return
        respuestas[pregunta.id] = opcion

        if (esUltima) {
            terminar(onTerminado)
        } else {
            indice++
            elegida = null
        }
    }

    private fun terminar(onTerminado: (Int) -> Unit) {
        viewModelScope.launch {
            guardando = true
            val intento = db.respuestaDao().intentosUsados(usuarioId, asignatura, unidad) + 1

            val lista = preguntas.map { p ->
                val opcion = respuestas.getValue(p.id)
                Respuesta(
                    usuarioId = usuarioId,
                    preguntaId = p.id,
                    opcionElegida = opcion,
                    esCorrecta = opcion == p.correcta,
                    intento = intento
                )
            }
            db.respuestaDao().insertarTodas(lista)

            val rendimientos = db.respuestaDao()
                .rendimientoPorOa(usuarioId, intento, asignatura, unidad)
            val fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            db.informeDao().insertarTodos(
                GeneradorInforme.generar(usuarioId, rendimientos, fecha)
            )

            guardando = false
            onTerminado(intento)
        }
    }
}