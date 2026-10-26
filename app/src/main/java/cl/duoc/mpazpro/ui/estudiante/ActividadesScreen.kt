package cl.duoc.mpazpro.ui.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.entity.Actividad
import cl.duoc.mpazpro.data.local.enums.Asignatura
import cl.duoc.mpazpro.data.local.enums.Opcion
import cl.duoc.mpazpro.ui.components.AppScaffold

private fun textoDe(a: Actividad, op: Opcion) = when (op) {
    Opcion.A -> a.opcionA
    Opcion.B -> a.opcionB
    Opcion.C -> a.opcionC
    Opcion.D -> a.opcionD
}

@Composable
fun ActividadesScreen(
    db: AppDatabase,
    usuarioId: Int,
    asignatura: Asignatura,
    unidad: String,
    onBack: () -> Unit
) {
    val vm: ActividadesViewModel = viewModel(
        factory = viewModelFactory {
            initializer { ActividadesViewModel(db, usuarioId, asignatura, unidad) }
        }
    )
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(vm.mensaje) {
        vm.mensaje?.let {
            snackbar.showSnackbar(it)
            vm.mensajeMostrado()
        }
    }

    AppScaffold(
        titulo = "Actividades y desafíos",
        onBack = onBack,
        snackbarHostState = snackbar
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (vm.cargado && vm.actividades.isEmpty()) {
                item { Text("Aún no hay actividades disponibles para esta unidad.") }
            }

            items(vm.actividades) { a ->
                val registro = vm.progreso[a.id]
                val elegida = vm.elegidas[a.id]

                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "${a.tipo.emoji} ${a.titulo}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                if (registro != null) "✅ Realizada" else "⏳ Pendiente",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text(a.enunciado)

                        Opcion.entries.forEach { op ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = elegida == op,
                                        onClick = { vm.elegir(a.id, op) },
                                        role = Role.RadioButton
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = elegida == op, onClick = null)
                                Spacer(Modifier.width(8.dp))
                                Text("${op.name}) ${textoDe(a, op)}")
                            }
                        }

                        Button(
                            onClick = { vm.comprobar(a) },
                            enabled = elegida != null,
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Comprobar") }

                        if (registro != null && registro.opcionElegida == elegida) {
                            if (registro.esCorrecta) {
                                Text("🎉 ¡Correcto!", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                            } else {
                                Text(
                                    "Casi. La respuesta correcta es ${a.correcta.name}) ${textoDe(a, a.correcta)}",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}