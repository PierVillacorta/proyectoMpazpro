package cl.duoc.mpazpro.ui.estudiante

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.entity.Actividad
import cl.duoc.mpazpro.data.local.entity.ProgresoActividad
import cl.duoc.mpazpro.data.local.enums.Asignatura
import cl.duoc.mpazpro.data.local.enums.Opcion
import kotlinx.coroutines.launch

class ActividadesViewModel(
    private val db: AppDatabase,
    private val usuarioId: Int,
    private val asignatura: Asignatura,
    private val unidad: String
) : ViewModel() {

    var actividades by mutableStateOf<List<Actividad>>(emptyList())
        private set
    var progreso by mutableStateOf<Map<Int, ProgresoActividad>>(emptyMap())
        private set
    var cargado by mutableStateOf(false)
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set

    val elegidas = mutableStateMapOf<Int, Opcion>()

    init {
        viewModelScope.launch {
            actividades = db.actividadDao().visibles(usuarioId, asignatura, unidad)
            progreso = db.actividadDao().progresoDe(usuarioId).associateBy { it.actividadId }
            progreso.values.forEach { elegidas[it.actividadId] = it.opcionElegida }
            cargado = true
        }
    }

    fun elegir(actividadId: Int, opcion: Opcion) {
        elegidas[actividadId] = opcion
    }

    fun comprobar(a: Actividad) {
        val opcion = elegidas[a.id] ?: return
        viewModelScope.launch {
            val registro = ProgresoActividad(usuarioId, a.id, opcion, opcion == a.correcta)
            db.actividadDao().guardarProgreso(registro)
            progreso = progreso + (a.id to registro)
            mensaje = "✅ Actividad completada"
        }
    }

    fun mensajeMostrado() {
        mensaje = null
    }
}