package cl.duoc.mpazpro.ui.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.dto.RespuestaDetalle
import cl.duoc.mpazpro.data.local.entity.Pregunta
import cl.duoc.mpazpro.data.local.enums.Asignatura
import cl.duoc.mpazpro.data.local.enums.Opcion
import cl.duoc.mpazpro.ui.components.AppScaffold

private fun textoOpcion(p: Pregunta, o: Opcion): String = when (o) {
    Opcion.A -> p.opcionA
    Opcion.B -> p.opcionB
    Opcion.C -> p.opcionC
    Opcion.D -> p.opcionD
}

@Composable
fun ResultadoScreen(
    db: AppDatabase,
    usuarioId: Int,
    asignatura: Asignatura,
    unidad: String,
    intento: Int,
    onVolver: () -> Unit
) {
    var detalle by remember { mutableStateOf<List<RespuestaDetalle>>(emptyList()) }

    LaunchedEffect(Unit) {
        detalle = db.respuestaDao().detalle(usuarioId, intento, asignatura, unidad)
    }

    val correctas = detalle.count { it.esCorrecta }
    val verde = Color(0xFF2E7D32)

    AppScaffold(titulo = "Resultado", onBack = onVolver) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "Respondiste bien $correctas de ${detalle.size}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text("Intento $intento")
            }

            items(detalle) { d ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(d.pregunta.enunciado, fontWeight = FontWeight.Bold)
                        if (d.esCorrecta) {
                            Text(
                                "✓ Correcta: ${textoOpcion(d.pregunta, d.opcionElegida)}",
                                color = verde
                            )
                        } else {
                            Text(
                                "✗ Tu respuesta: ${textoOpcion(d.pregunta, d.opcionElegida)}",
                                color = MaterialTheme.colorScheme.error
                            )
                            Text("Respuesta correcta: ${textoOpcion(d.pregunta, d.pregunta.correcta)}")
                        }
                    }
                }
            }

            item {
                Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
                    Text("Volver a la unidad")
                }
            }
        }
    }
}