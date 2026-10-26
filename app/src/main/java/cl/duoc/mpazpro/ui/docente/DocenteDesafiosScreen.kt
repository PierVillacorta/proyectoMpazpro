package cl.duoc.mpazpro.ui.docente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.mpazpro.data.local.AppDatabase

@Composable
fun DocenteDesafiosScreen(db: AppDatabase, usuarioId: Int) {
    val vm: DocenteDesafiosViewModel = viewModel(
        factory = viewModelFactory { initializer { DocenteDesafiosViewModel(db, usuarioId) } }
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Activa los desafíos que quieres asignar a tu curso. Las actividades de práctica " +
                        "están siempre disponibles para los objetivos que ya asignaste.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (vm.desafios.isEmpty()) {
            item { Text("No hay desafíos disponibles") }
        }

        items(vm.desafios) { d ->
            Card(Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("🚀 ${d.actividad.titulo}", fontWeight = FontWeight.Bold)
                        Text(
                            "${d.oa.asignatura.nombre} · ${d.oa.codigo}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = d.actividad.id in vm.asignados,
                        onCheckedChange = { vm.alternar(d.actividad.id) }
                    )
                }
            }
        }
    }
}