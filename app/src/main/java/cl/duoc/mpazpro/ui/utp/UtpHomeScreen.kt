package cl.duoc.mpazpro.ui.utp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.enums.EstadoContenido
import cl.duoc.mpazpro.ui.components.AppScaffold
import androidx.compose.animation.AnimatedVisibility
@Composable
fun UtpHomeScreen(
    db: AppDatabase,
    usuarioId: Int,
    onLogout: () -> Unit,
    onFormulario: (String) -> Unit = {}
) {
    val vm: UtpViewModel = viewModel(
        factory = viewModelFactory { initializer { UtpViewModel(db) } }
    )

    AppScaffold(titulo = "Panel UTP", onLogout = onLogout) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Revisa que los contenidos y las preguntas correspondan a su objetivo de aprendizaje. " +
                            "Solo lo autorizado puede ser asignado por los docentes.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                OutlinedButton(
                    onClick = { onFormulario("contenido") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar contenido")
                }
            }


            items(vm.items, key = { it.oa.id }) { r ->
                var expandido by remember { mutableStateOf(false) }
                val autorizado = r.oa.estado == EstadoContenido.AUTORIZADO ||
                        r.oa.estado == EstadoContenido.PUBLICADO

                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            "${r.oa.asignatura.nombre} · ${r.oa.unidad}",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            "${r.oa.codigo}: ${r.oa.descripcion}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            "Estado: ${r.oa.estado.etiqueta}",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${r.contenidos.size} contenido(s) · ${r.preguntas.size} pregunta(s)",
                            style = MaterialTheme.typography.bodySmall
                        )

                        TextButton(onClick = { expandido = !expandido }) {
                            Text(if (expandido) "Ocultar detalle" else "Ver detalle")
                        }

                        AnimatedVisibility(visible = expandido) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                r.contenidos.forEach { c ->
                                    Text(c.titulo, fontWeight = FontWeight.Bold)
                                    Text(c.explicacion)
                                    Text(
                                        "Origen: ${c.origen.etiqueta}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                r.preguntas.forEach { p ->
                                    Text("• ${p.enunciado}  (correcta: ${p.correcta.name})")
                                }
                                Spacer(Modifier.height(4.dp))
                            }
                        }

                        if (autorizado) {
                            OutlinedButton(
                                onClick = { vm.cambiarEstado(r.oa.id, EstadoContenido.EN_REVISION) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Quitar autorización")
                            }
                        } else {
                            Button(
                                onClick = { vm.cambiarEstado(r.oa.id, EstadoContenido.AUTORIZADO) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Autorizar")
                            }
                        }
                    }
                }
            }
        }
    }
}