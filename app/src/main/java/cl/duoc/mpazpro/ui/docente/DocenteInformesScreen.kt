package cl.duoc.mpazpro.ui.docente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.enums.Clasificacion
import cl.duoc.mpazpro.data.local.enums.EstadoInforme
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext

@Composable
fun DocenteInformesScreen(db: AppDatabase, usuarioId: Int) {
    val vm: DocenteInformesViewModel = viewModel(
        factory = viewModelFactory { initializer { DocenteInformesViewModel(db, usuarioId) } }
    )
    val context = LocalContext.current
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        if (uri != null) {
            context.contentResolver.openOutputStream(uri)?.use { salida ->
                salida.write(vm.comoCsv().toByteArray(Charsets.UTF_8))
            }
            Toast.makeText(context, "Informe guardado", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedButton(
                onClick = { lanzador.launch("informes_pedagogicos.csv") },
                enabled = vm.informes.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("⬇ Descargar informes (CSV)")
            }
        }
        item {
            Text(
                "Estos informes se generan por reglas según las respuestas correctas e incorrectas. " +
                        "No son un diagnóstico: requieren tu validación.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (vm.informes.isEmpty()) {
            item { Text("Aún no hay informes generados") }
        }

        items(vm.informes) { i ->
            val colorClasificacion = when (i.clasificacion) {
                Clasificacion.FORTALEZA -> Color(0xFF2E7D32)
                Clasificacion.NECESITA_REFUERZO -> MaterialTheme.colorScheme.error
                Clasificacion.EN_DESARROLLO -> MaterialTheme.colorScheme.onSurface
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "${i.alumno} · ${i.asignatura.nombre}",
                        fontWeight = FontWeight.Bold
                    )
                    Text("${i.unidad} · ${i.codigoOa}", style = MaterialTheme.typography.bodySmall)
                    Text("${i.correctas} correctas · ${i.incorrectas} incorrectas · ${i.porcentaje} %")
                    Text(i.clasificacion.etiqueta, color = colorClasificacion, fontWeight = FontWeight.Bold)
                    Text(
                        "Generado: ${i.fecha} · ${i.estado.etiqueta}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    when (i.estado) {
                        EstadoInforme.VALIDADO -> Text(
                            "Validado por ${i.docenteResponsable}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        EstadoInforme.PENDIENTE_VALIDACION -> Button(onClick = { vm.validar(i.id) }) {
                            Text("Validar informe")
                        }
                        EstadoInforme.GENERADO -> Unit
                    }
                }
            }
        }
    }
}