package cl.duoc.mpazpro.ui.estudiante

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.enums.Asignatura
import cl.duoc.mpazpro.data.local.enums.Opcion
import cl.duoc.mpazpro.ui.components.AppScaffold

@Composable
fun PruebaScreen(
    db: AppDatabase,
    usuarioId: Int,
    asignatura: Asignatura,
    unidad: String,
    onBack: () -> Unit,
    onTerminada: (Int) -> Unit
) {
    val vm: PruebaViewModel = viewModel(
        factory = viewModelFactory {
            initializer { PruebaViewModel(db, usuarioId, asignatura, unidad) }
        }
    )

    AppScaffold(titulo = "Prueba final", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            when {
                vm.bloqueada -> Text("Ya usaste todos tus intentos para esta prueba.")

                vm.preguntas.isEmpty() -> CircularProgressIndicator()

                else -> {
                    val total = vm.preguntas.size
                    val progreso by animateFloatAsState(
                        targetValue = (vm.indice + 1f) / total,
                        animationSpec = tween(400),
                        label = "progreso"
                    )

                    Text(
                        "Pregunta ${vm.indice + 1} de $total",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = progreso,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(16.dp))

                    AnimatedContent(
                        targetState = vm.indice,
                        transitionSpec = {
                            (slideInHorizontally(tween(300)) { it } + fadeIn(tween(300))) togetherWith
                                    (slideOutHorizontally(tween(300)) { -it } + fadeOut(tween(300)))
                        },
                        label = "pregunta"
                    ) { indice ->
                        val pregunta = vm.preguntas[indice]

                        Column(Modifier.verticalScroll(rememberScrollState())) {
                            Text(pregunta.enunciado, style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(16.dp))

                            val opciones = listOf(
                                Opcion.A to pregunta.opcionA,
                                Opcion.B to pregunta.opcionB,
                                Opcion.C to pregunta.opcionC,
                                Opcion.D to pregunta.opcionD
                            )
                            opciones.forEach { (letra, texto) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .selectable(
                                            selected = vm.elegida == letra,
                                            onClick = { vm.elegida = letra },
                                            role = Role.RadioButton
                                        )
                                        .padding(vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(selected = vm.elegida == letra, onClick = null)
                                    Spacer(Modifier.width(8.dp))
                                    Text("${letra.name}) $texto")
                                }
                            }

                            Spacer(Modifier.height(24.dp))
                            Button(
                                onClick = { vm.avanzar(onTerminada) },
                                enabled = vm.elegida != null && !vm.guardando,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(if (indice == vm.preguntas.lastIndex) "Finalizar" else "Siguiente")
                            }
                        }
                    }
                }
            }
        }
    }
}