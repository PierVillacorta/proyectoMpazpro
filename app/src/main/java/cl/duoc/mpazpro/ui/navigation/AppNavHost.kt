package cl.duoc.mpazpro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.enums.Rol
import cl.duoc.mpazpro.ui.docente.DocenteHomeScreen
import cl.duoc.mpazpro.ui.estudiante.EstudianteHomeScreen
import cl.duoc.mpazpro.ui.login.LoginScreen
import cl.duoc.mpazpro.ui.utp.UtpHomeScreen

@Composable
fun AppNavHost(db: AppDatabase) {
    val navController = rememberNavController()
    val argId = listOf(navArgument("usuarioId") { type = NavType.IntType })

    NavHost(navController = navController, startDestination = Rutas.LOGIN) {

        composable(Rutas.LOGIN) {
            LoginScreen(db = db, onLoginExitoso = { usuario ->
                val destino = when (usuario.rol) {
                    Rol.ESTUDIANTE -> Rutas.estudiante(usuario.id)
                    Rol.DOCENTE -> Rutas.docente(usuario.id)
                    Rol.UTP -> Rutas.utp(usuario.id)
                }
                navController.navigate(destino) {
                    popUpTo(Rutas.LOGIN) { inclusive = true }
                }
            })
        }

        composable(Rutas.ESTUDIANTE, arguments = argId) { entry ->
            EstudianteHomeScreen(usuarioId = entry.arguments?.getInt("usuarioId") ?: 0)
        }
        composable(Rutas.DOCENTE, arguments = argId) { entry ->
            DocenteHomeScreen(usuarioId = entry.arguments?.getInt("usuarioId") ?: 0)
        }
        composable(Rutas.UTP, arguments = argId) { entry ->
            UtpHomeScreen(usuarioId = entry.arguments?.getInt("usuarioId") ?: 0)
        }
    }
}