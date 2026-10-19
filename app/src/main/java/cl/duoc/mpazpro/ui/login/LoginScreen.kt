package cl.duoc.mpazpro.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.entity.Usuario

@Composable
fun LoginScreen(db: AppDatabase, onLoginExitoso: (Usuario) -> Unit) {
    val vm: LoginViewModel = viewModel(
        factory = viewModelFactory { initializer { LoginViewModel(db) } }
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("MPAZ PRO 6.º", style = MaterialTheme.typography.headlineMedium)
        Text("Escuela Escritora Marcela Paz", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = vm.nombre,
            onValueChange = { vm.nombre = it },
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = vm.clave,
            onValueChange = { vm.clave = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        vm.error?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { vm.iniciarSesion(onLoginExitoso) },
            enabled = !vm.cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ingresar")
        }

        Spacer(Modifier.height(24.dp))
        Text(
            "Usuarios de prueba: alumno1, docente.demo, utp.demo (clave 1234)",
            style = MaterialTheme.typography.bodySmall
        )
    }
}