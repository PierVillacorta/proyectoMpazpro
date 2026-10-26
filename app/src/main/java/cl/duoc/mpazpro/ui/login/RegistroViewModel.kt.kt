package cl.duoc.mpazpro.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.entity.Usuario
import cl.duoc.mpazpro.data.local.enums.Rol
import kotlinx.coroutines.launch

class RegistroViewModel(private val db: AppDatabase) : ViewModel() {
    var nombre by mutableStateOf("")
    var clave by mutableStateOf("")
    var confirmar by mutableStateOf("")
    var rol by mutableStateOf(Rol.ESTUDIANTE)
    var curso by mutableStateOf("6.º A")
    var error by mutableStateOf<String?>(null)
    var cargando by mutableStateOf(false)

    fun registrar(onExito: () -> Unit) {
        val n = nombre.trim()
        error = when {
            n.length < 4 -> "El usuario debe tener al menos 4 caracteres"
            !n.all { it.isLetterOrDigit() || it == '.' || it == '_' } ->
                "Usa solo letras, números, punto o guion bajo"
            clave.length < 4 -> "La contraseña debe tener al menos 4 caracteres"
            clave != confirmar -> "Las contraseñas no coinciden"
            else -> null
        }
        if (error != null) return

        viewModelScope.launch {
            cargando = true
            if (db.usuarioDao().existeNombre(n) > 0) {
                error = "Ese usuario ya existe"
            } else {
                db.usuarioDao().insertar(
                    Usuario(nombre = n, clave = clave, rol = rol, curso = curso)
                )
                onExito()
            }
            cargando = false
        }
    }
}