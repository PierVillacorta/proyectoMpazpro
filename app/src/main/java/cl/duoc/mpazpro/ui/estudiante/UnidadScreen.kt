package cl.duoc.mpazpro.ui.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.entity.Contenido
import cl.duoc.mpazpro.data.local.entity.ObjetivoAprendizaje
import cl.duoc.mpazpro.data.local.enums.Asignatura
import cl.duoc.mpazpro.ui.components.AppScaffold
import kotlinx.coroutines.launch

@Composable
fun UnidadScreen(
    db: AppDatabase,
    usuarioId: Int,
    asignatura: Asignatura,
    unidad: String,
    onBack: () -> Unit,
    onRendirPrueba: () -> Unit
) {
    var temas by remember {
        mutableStateOf<List<Pair<ObjetivoAprendizaje, List<Contenido>>>>(emptyList())
    }
    var usados by remember { mutableIntStateOf(0) }
    var maximo by remember { mutableIntStateOf(1) }

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        temas = db.objetivoDao().asignadosAPorUnidad(usuarioId, asignatura, unidad)
            .map { oa -> oa to db.contenidoDao().porOa(oa.id) }
        usados = db.respuestaDao().intentosUsados(usuarioId, asignatura, unidad)
        maximo = db.configPruebaDao().obtener(asignatura, unidad)?.maxIntentos ?: 1
    }

    AppScaffold(
        titulo = "${asignatura.nombre} · $unidad",
        onBack = onBack,
        snackbarHostState = snackbar
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(temas) { (oa, contenidos) ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "${oa.codigo}: ${oa.descripcion}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        contenidos.forEach { c ->
                            Text(c.titulo, fontWeight = FontWeight.Bold)
                            Text(c.explicacion)
                            Text("Ejemplo: ${c.ejemplo}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Prueba final", style = MaterialTheme.typography.titleMedium)
                        Text("Intentos usados: $usados de $maximo")
                        Button(
                            onClick = {
                                if (usados < maximo) {
                                    onRendirPrueba()
                                } else {
                                    scope.launch {
                                        snackbar.showSnackbar("Ya usaste todos tus intentos")
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Rendir prueba")
                        }
                    }
                }
            }
        }
    }
}