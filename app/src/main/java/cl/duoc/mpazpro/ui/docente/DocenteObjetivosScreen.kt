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
fun DocenteObjetivosScreen(db: AppDatabase, usuarioId: Int, onFormulario: (String) -> Unit = {}) {
    val vm: DocenteObjetivosViewModel = viewModel(
        factory = viewModelFactory { initializer { DocenteObjetivosViewModel(db, usuarioId) } }
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedButton(
                onClick = { onFormulario("actividad") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Crear nueva actividad")
            }
        }

        item {
            Text(
                "Activa los objetivos que quieres asignar a tu curso y define cuántos intentos tendrán en la prueba final.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        items(vm.unidades) { u ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "${u.asignatura.nombre} · ${u.unidad}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Intentos de la prueba", Modifier.weight(1f))
                        OutlinedButton(onClick = { vm.cambiarIntentos(u, u.maxIntentos - 1) }) {
                            Text("−")
                        }
                        Text("${u.maxIntentos}", Modifier.padding(horizontal = 12.dp))
                        OutlinedButton(onClick = { vm.cambiarIntentos(u, u.maxIntentos + 1) }) {
                            Text("+")
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    u.objetivos.forEach { oa ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(oa.codigo, fontWeight = FontWeight.Bold)
                                Text(oa.descripcion, style = MaterialTheme.typography.bodySmall)
                            }
                            Switch(
                                checked = oa.id in vm.asignados,
                                onCheckedChange = { vm.alternarAsignacion(oa.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}