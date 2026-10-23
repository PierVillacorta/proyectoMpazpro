package cl.duoc.mpazpro.ui.utp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.entity.Contenido
import cl.duoc.mpazpro.data.local.entity.ObjetivoAprendizaje
import cl.duoc.mpazpro.data.local.entity.Pregunta
import cl.duoc.mpazpro.data.local.enums.EstadoContenido
import kotlinx.coroutines.launch

data class RevisionUi(
    val oa: ObjetivoAprendizaje,
    val contenidos: List<Contenido>,
    val preguntas: List<Pregunta>
)

class UtpViewModel(private val db: AppDatabase) : ViewModel() {

    var items by mutableStateOf<List<RevisionUi>>(emptyList())
        private set

    init {
        viewModelScope.launch { recargar() }
    }

    private suspend fun recargar() {
        items = db.objetivoDao().todos().map { oa ->
            RevisionUi(
                oa = oa,
                contenidos = db.contenidoDao().porOa(oa.id),
                preguntas = db.preguntaDao().porOa(oa.id)
            )
        }
    }

    fun cambiarEstado(oaId: Int, estado: EstadoContenido) {
        viewModelScope.launch {
            db.objetivoDao().cambiarEstado(oaId, estado)
            recargar()
        }
    }
}