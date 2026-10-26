package cl.duoc.mpazpro.ui.estudiante

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.entity.Contenido
import cl.duoc.mpazpro.data.local.entity.ObjetivoAprendizaje
import cl.duoc.mpazpro.data.local.enums.Asignatura
import cl.duoc.mpazpro.ui.components.AppScaffold
import cl.duoc.mpazpro.ui.theme.colorTema
import cl.duoc.mpazpro.ui.theme.colorTexto
import cl.duoc.mpazpro.ui.theme.emoji
import kotlinx.coroutines.launch

@Composable
private fun ImagenContenido(nombre: String, descripcion: String) {
    val context = LocalContext.current
    val id = remember(nombre) {
        context.resources.getIdentifier(nombre, "drawable", context.packageName)
    }
    if (id != 0) {
        Image(
            painter = painterResource(id),
            contentDescription = descripcion,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth().heightIn(max = 180.dp)
        )
    }
}

@Composable
private fun BotonAudio(nombre: String) {
    val context = LocalContext.current
    val id = remember(nombre) {
        context.resources.getIdentifier(nombre, "raw", context.packageName)
    }
    if (id == 0) return

    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    var sonando by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { player?.release() }
    }

    OutlinedButton(onClick = {
        if (sonando) {
            player?.release()
            player = null
            sonando = false
        } else {
            val mp = MediaPlayer.create(context, id) ?: return@OutlinedButton
            mp.setOnCompletionListener {
                it.release()
                player = null
                sonando = false
            }
            mp.start()
            player = mp
            sonando = true
        }
    }) {
        Text(if (sonando) "⏹ Detener audio" else "🔊 Escuchar")
    }
}

@Composable
fun UnidadScreen(
    db: AppDatabase,
    usuarioId: Int,
    asignatura: Asignatura,
    unidad: String,
    onBack: () -> Unit,
    onRendirPrueba: () -> Unit,
    onActividades: () -> Unit = {}
) {
    var temas by remember {
        mutableStateOf<List<Pair<ObjetivoAprendizaje, List<Contenido>>>>(emptyList())
    }
    var usados by remember { mutableIntStateOf(0) }
    var maximo by remember { mutableIntStateOf(1) }
    var totalActividades by remember { mutableIntStateOf(0) }
    var hechas by remember { mutableIntStateOf(0) }

    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        temas = db.objetivoDao().asignadosAPorUnidad(usuarioId, asignatura, unidad)
            .map { oa -> oa to db.contenidoDao().porOa(oa.id) }
        usados = db.respuestaDao().intentosUsados(usuarioId, asignatura, unidad)
        maximo = db.configPruebaDao().obtener(asignatura, unidad)?.maxIntentos ?: 1

        val visibles = db.actividadDao().visibles(usuarioId, asignatura, unidad)
        val hechasIds = db.actividadDao().progresoDe(usuarioId).map { it.actividadId }.toSet()
        totalActividades = visibles.size
        hechas = visibles.count { it.id in hechasIds }
    }

    // Avance = actividades realizadas + prueba final (si ya la rindió)
    val pasos = totalActividades + 1
    val completados = hechas + if (usados > 0) 1 else 0
    val fraccion = completados.toFloat() / pasos
    val estado = when {
        completados == 0 -> "Sin iniciar"
        completados == pasos -> "Completado"
        else -> "En progreso"
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
            // 1) Encabezado de color
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = asignatura.colorTema)
                ) {
                    Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(asignatura.emoji, fontSize = 36.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                asignatura.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = asignatura.colorTexto
                            )
                            Text(unidad, color = asignatura.colorTexto.copy(alpha = 0.9f))
                        }
                    }
                }
            }

            // 2) Mi avance
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("📈 Mi avance", style = MaterialTheme.typography.titleMedium)
                        Text("Estado: $estado")
                        LinearProgressIndicator(
                            progress = { fraccion },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            "$completados de $pasos pasos (actividades + prueba final)",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // 3) Contenidos
            items(temas) { (oa, contenidos) ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "🎯 ${oa.codigo}: ${oa.descripcion}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (oa.eje.isNotBlank()) {
                            Text("Eje: ${oa.eje} · ${oa.nivel}", style = MaterialTheme.typography.bodySmall)
                        }
                        if (oa.textoEscolar.isNotBlank()) {
                            Text("Referencia: ${oa.textoEscolar}", style = MaterialTheme.typography.bodySmall)
                        }
                        contenidos.forEach { c ->
                            Text(c.titulo, fontWeight = FontWeight.Bold)
                            c.imagen?.let { ImagenContenido(it, c.titulo) }
                            Text(c.explicacion)
                            Text("Ejemplo: ${c.ejemplo}", style = MaterialTheme.typography.bodySmall)
                            c.audio?.let { BotonAudio(it) }
                            Text(
                                "Origen del material: ${c.origen.etiqueta}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            // 4) Actividades y desafíos
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("📝 Actividades y desafíos", style = MaterialTheme.typography.titleMedium)
                        Text("Realizadas: $hechas de $totalActividades")
                        OutlinedButton(onClick = onActividades, modifier = Modifier.fillMaxWidth()) {
                            Text("Ir a las actividades")
                        }
                    }
                }
            }

            // 5) Prueba final
            item {
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("🏆 Prueba final", style = MaterialTheme.typography.titleMedium)
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