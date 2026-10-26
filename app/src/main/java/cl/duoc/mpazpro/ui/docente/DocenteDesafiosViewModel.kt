package cl.duoc.mpazpro.ui.docente

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.entity.Actividad
import cl.duoc.mpazpro.data.local.entity.AsignacionDesafio
import cl.duoc.mpazpro.data.local.entity.ObjetivoAprendizaje
import kotlinx.coroutines.launch

data class DesafioUi(val actividad: Actividad, val oa: ObjetivoAprendizaje)

class DocenteDesafiosViewModel(
    private val db: AppDatabase,
    private val usuarioId: Int
) : ViewModel() {

    var desafios by mutableStateOf<List<DesafioUi>>(emptyList())
        private set
    var asignados by mutableStateOf<Set<Int>>(emptySet())
        private set

    private var curso = ""

    init {
        viewModelScope.launch {
            curso = db.usuarioDao().porId(usuarioId)?.curso ?: ""
            asignados = db.actividadDao().desafiosAsignados(curso).toSet()
            val oas = db.objetivoDao().disponibles().associateBy { it.id }
            desafios = db.actividadDao().desafios()
                .mapNotNull { a -> oas[a.oaId]?.let { DesafioUi(a, it) } }
        }
    }

    fun alternar(actividadId: Int) {
        viewModelScope.launch {
            if (actividadId in asignados) {
                db.actividadDao().quitarDesafio(actividadId, curso)
                asignados = asignados - actividadId
            } else {
                db.actividadDao().asignarDesafio(AsignacionDesafio(actividadId, curso))
                asignados = asignados + actividadId
            }
        }
    }
}