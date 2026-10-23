package cl.duoc.mpazpro.ui.docente

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.entity.Asignacion
import cl.duoc.mpazpro.data.local.entity.ConfigPrueba
import cl.duoc.mpazpro.data.local.entity.ObjetivoAprendizaje
import cl.duoc.mpazpro.data.local.enums.Asignatura
import kotlinx.coroutines.launch

data class UnidadUi(
    val asignatura: Asignatura,
    val unidad: String,
    val objetivos: List<ObjetivoAprendizaje>,
    val maxIntentos: Int
)

class DocenteObjetivosViewModel(
    private val db: AppDatabase,
    private val usuarioId: Int
) : ViewModel() {

    var unidades by mutableStateOf<List<UnidadUi>>(emptyList())
        private set
    var asignados by mutableStateOf<Set<Int>>(emptySet())
        private set

    private var curso = ""

    init {
        viewModelScope.launch {
            curso = db.usuarioDao().porId(usuarioId)?.curso ?: ""
            asignados = db.asignacionDao().idsAsignados(curso).toSet()
            unidades = db.objetivoDao().disponibles()
                .groupBy { it.asignatura to it.unidad }
                .map { (clave, objetivos) ->
                    val (asignatura, unidad) = clave
                    val max = db.configPruebaDao().obtener(asignatura, unidad)?.maxIntentos ?: 1
                    UnidadUi(asignatura, unidad, objetivos, max)
                }
        }
    }

    fun alternarAsignacion(oaId: Int) {
        viewModelScope.launch {
            if (oaId in asignados) {
                db.asignacionDao().quitar(oaId, curso)
                asignados = asignados - oaId
            } else {
                db.asignacionDao().asignar(Asignacion(oaId, curso))
                asignados = asignados + oaId
            }
        }
    }

    fun cambiarIntentos(u: UnidadUi, nuevo: Int) {
        val valor = nuevo.coerceIn(1, 5)
        viewModelScope.launch {
            db.configPruebaDao().guardar(ConfigPrueba(u.asignatura, u.unidad, valor))
            unidades = unidades.map {
                if (it.asignatura == u.asignatura && it.unidad == u.unidad) it.copy(maxIntentos = valor) else it
            }
        }
    }
}