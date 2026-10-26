package cl.duoc.mpazpro.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.entity.Usuario
import kotlinx.coroutines.launch

class LoginViewModel(private val db: AppDatabase) : ViewModel() {

    var nombre by mutableStateOf("")
    var clave by mutableStateOf("")
    var error by mutableStateOf<String?>(null)
    var cargando by mutableStateOf(false)

    fun iniciarSesion(onExito: (Usuario) -> Unit) {
        if (nombre.isBlank() || clave.isBlank()) {
            error = "Completa usuario y contraseña"
            return
        }
        viewModelScope.launch {
            cargando = true
            val usuario = db.usuarioDao().login(nombre.trim(), clave)
            if (usuario == null) {
                error = "Usuario o contraseña incorrectos"
            } else {
                error = null
                onExito(usuario)
            }
        }
    }
}