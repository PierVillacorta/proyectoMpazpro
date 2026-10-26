package cl.duoc.mpazpro.ui.docente

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.dto.InformeDetalle
import cl.duoc.mpazpro.data.local.entity.Usuario
import kotlinx.coroutines.launch

class DocenteInformesViewModel(
    private val db: AppDatabase,
    private val usuarioId: Int
) : ViewModel() {

    var informes by mutableStateOf<List<InformeDetalle>>(emptyList())
        private set

    private var docente: Usuario? = null

    init {
        viewModelScope.launch {
            docente = db.usuarioDao().porId(usuarioId)
            informes = db.informeDao().delCurso(docente?.curso ?: "")
        }
    }

    fun validar(informeId: Int) {
        viewModelScope.launch {
            db.informeDao().validar(informeId, docente?.nombre ?: "")
            informes = db.informeDao().delCurso(docente?.curso ?: "")
        }
    }
    fun comoCsv(): String {
        val encabezado = "Estudiante,Asignatura,Unidad,OA,Correctas,Incorrectas,Porcentaje," +
                "Clasificacion,Fecha,Estado,Docente responsable"
        val filas = informes.joinToString("\n") { i ->
            listOf(
                i.alumno, i.asignatura.nombre, i.unidad, i.codigoOa,
                i.correctas.toString(), i.incorrectas.toString(), i.porcentaje.toString(),
                i.clasificacion.etiqueta, i.fecha, i.estado.etiqueta, i.docenteResponsable ?: ""
            ).joinToString(",") { "\"$it\"" }
        }
        return "\uFEFF$encabezado\n$filas"   // el BOM hace que Excel lea bien las tildes
    }
}