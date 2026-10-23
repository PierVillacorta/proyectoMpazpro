package cl.duoc.mpazpro.ui.estudiante

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.enums.Asignatura
import cl.duoc.mpazpro.ui.components.AppScaffold

@Composable
fun EstudianteHomeScreen(
    db: AppDatabase,
    usuarioId: Int,   // se usará más adelante para mostrar el avance
    onUnidadClick: (Asignatura, String) -> Unit,
    onLogout: () -> Unit
) {
    var unidades by remember { mutableStateOf<List<Pair<Asignatura, String>>>(emptyList()) }
    var cargando by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        unidades = db.objetivoDao().asignadosA(usuarioId)
            .map { it.asignatura to it.unidad }
            .distinct()
        cargando = false
    }

    AppScaffold(titulo = "Mis asignaturas", onLogout = onLogout) { padding ->
        when {
            cargando -> Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                CircularProgressIndicator()
            }
            unidades.isEmpty() -> Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
                Text("Tu docente aún no ha asignado contenidos")
            }
            else -> LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(unidades) { (asignatura, unidad) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onUnidadClick(asignatura, unidad) }
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(asignatura.nombre, style = MaterialTheme.typography.titleMedium)
                            Text(unidad, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}