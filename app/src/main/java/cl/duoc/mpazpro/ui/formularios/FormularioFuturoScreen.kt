package cl.duoc.mpazpro.ui.formularios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.mpazpro.ui.components.AppScaffold
import kotlinx.coroutines.launch

@Composable
fun FormularioFuturoScreen(tipo: String, onBack: () -> Unit) {
    val (titulo, campos) = when (tipo) {
        "contenido" -> "Nuevo contenido" to listOf(
            "Título del contenido", "Explicación", "Ejemplo", "Objetivo de aprendizaje"
        )
        "actividad" -> "Nueva actividad" to listOf(
            "Enunciado", "Alternativa A", "Alternativa B",
            "Alternativa C", "Alternativa D", "Respuesta correcta (A-D)"
        )
        else -> "Recuperar contraseña" to listOf("Nombre de usuario")
    }

    val valores = remember(tipo) {
        mutableStateListOf<String>().apply { repeat(campos.size) { add("") } }
    }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    AppScaffold(titulo = titulo, onBack = onBack, snackbarHostState = snackbar) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            campos.forEachIndexed { i, etiqueta ->
                OutlinedTextField(
                    value = valores[i],
                    onValueChange = { valores[i] = it },
                    label = { Text(etiqueta) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Button(
                onClick = {
                    scope.launch { snackbar.showSnackbar("Para una futura actualización") }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}