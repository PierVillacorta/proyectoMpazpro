package cl.duoc.mpazpro.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.enums.Rol
import cl.duoc.mpazpro.ui.components.AppScaffold

@Composable
fun RegistroScreen(db: AppDatabase, onBack: () -> Unit) {
    val vm: RegistroViewModel = viewModel(
        factory = viewModelFactory { initializer { RegistroViewModel(db) } }
    )
    var creada by remember { mutableStateOf(false) }

    AppScaffold(titulo = "Crear cuenta", onBack = onBack) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Usa datos ficticios: no ingreses nombres, RUT ni correos reales.",
                style = MaterialTheme.typography.bodySmall
            )
            OutlinedTextField(
                value = vm.nombre,
                onValueChange = { vm.nombre = it },
                label = { Text("Usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = vm.clave,
                onValueChange = { vm.clave = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = vm.confirmar,
                onValueChange = { vm.confirmar = it },
                label = { Text("Confirmar contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Text("Perfil", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(Rol.ESTUDIANTE to "Estudiante", Rol.DOCENTE to "Docente")
                    .forEach { (r, etiqueta) ->
                        FilterChip(
                            selected = vm.rol == r,
                            onClick = { vm.rol = r },
                            label = { Text(etiqueta) }
                        )
                    }
            }

            Text("Curso", style = MaterialTheme.typography.labelLarge)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("6.º A").forEach { c ->
                    FilterChip(
                        selected = vm.curso == c,
                        onClick = { vm.curso = c },
                        label = { Text(c) }
                    )
                }
            }

            vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(
                onClick = { vm.registrar { creada = true } },
                enabled = !vm.cargando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarme")
            }
        }
    }

    if (creada) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Cuenta creada") },
            text = { Text("Ya puedes ingresar con tu usuario y contraseña.") },
            confirmButton = {
                TextButton(onClick = onBack) { Text("Ir a iniciar sesión") }
            }
        )
    }
}