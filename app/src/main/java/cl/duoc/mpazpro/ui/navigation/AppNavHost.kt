package cl.duoc.mpazpro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.enums.Asignatura
import cl.duoc.mpazpro.data.local.enums.Rol
import cl.duoc.mpazpro.ui.docente.DocenteHomeScreen
import cl.duoc.mpazpro.ui.estudiante.EstudianteHomeScreen
import cl.duoc.mpazpro.ui.estudiante.PruebaScreen
import cl.duoc.mpazpro.ui.estudiante.ResultadoScreen
import cl.duoc.mpazpro.ui.estudiante.UnidadScreen
import cl.duoc.mpazpro.ui.login.LoginScreen
import cl.duoc.mpazpro.ui.utp.UtpHomeScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import cl.duoc.mpazpro.ui.login.RegistroScreen
import cl.duoc.mpazpro.ui.formularios.FormularioFuturoScreen
import cl.duoc.mpazpro.ui.estudiante.ActividadesScreen
@Composable
fun AppNavHost(db: AppDatabase) {
    val navController = rememberNavController()

    val cerrarSesion = {
        navController.navigate(Rutas.LOGIN) {
            popUpTo(0) { inclusive = true }   // vacía TODA la pila
        }
    }

    val argUsuario = listOf(navArgument("usuarioId") { type = NavType.IntType })
    val argUnidad = argUsuario + listOf(
        navArgument("asignatura") { type = NavType.StringType },
        navArgument("unidad") { type = NavType.StringType }
    )
    val argResultado = argUnidad + listOf(
        navArgument("intento") { type = NavType.IntType }
    )

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) +
                    fadeIn(tween(300))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) +
                    fadeOut(tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) +
                    fadeIn(tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) +
                    fadeOut(tween(300))
        }
    )
        // ... tus composable(...) igual que antes
    {

        composable(Rutas.LOGIN) {
            LoginScreen(
                db = db,
                onLoginExitoso = { usuario ->
                    val destino = when (usuario.rol) {
                        Rol.ESTUDIANTE -> Rutas.estudiante(usuario.id)
                        Rol.DOCENTE -> Rutas.docente(usuario.id)
                        Rol.UTP -> Rutas.utp(usuario.id)
                    }
                    navController.navigate(destino) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                },
                onRegistro = { navController.navigate(Rutas.REGISTRO) },
                onOlvido = { navController.navigate(Rutas.formulario("recuperar")) }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(db = db, onBack = { navController.popBackStack() })
        }

        composable(
            Rutas.FORMULARIO,
            arguments = listOf(navArgument("tipo") { type = NavType.StringType })
        ) { entry ->
            FormularioFuturoScreen(
                tipo = entry.arguments!!.getString("tipo")!!,
                onBack = { navController.popBackStack() }
            )
        }

        // ---------- ESTUDIANTE ----------
        composable(Rutas.ESTUDIANTE, arguments = argUsuario) { entry ->
            val usuarioId = entry.arguments!!.getInt("usuarioId")
            EstudianteHomeScreen(
                db = db,
                usuarioId = usuarioId,
                onUnidadClick = { asignatura, unidad ->
                    navController.navigate(Rutas.unidad(usuarioId, asignatura, unidad))
                },
                onLogout = { cerrarSesion() }
            )
        }
        composable(Rutas.ACTIVIDADES, arguments = argUnidad) { entry ->
            val args = entry.arguments!!
            ActividadesScreen(
                db = db,
                usuarioId = args.getInt("usuarioId"),
                asignatura = Asignatura.valueOf(args.getString("asignatura")!!),
                unidad = args.getString("unidad")!!,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Rutas.UNIDAD, arguments = argUnidad) { entry ->
            val args = entry.arguments!!
            val usuarioId = args.getInt("usuarioId")
            val asignatura = Asignatura.valueOf(args.getString("asignatura")!!)
            val unidad = args.getString("unidad")!!
            UnidadScreen(
                db = db,
                usuarioId = usuarioId,
                asignatura = asignatura,
                unidad = unidad,
                onBack = { navController.popBackStack() },
                onRendirPrueba = {
                    navController.navigate(Rutas.prueba(usuarioId, asignatura, unidad))
                },
                onActividades = {
                    navController.navigate(Rutas.actividades(usuarioId, asignatura, unidad))
                }
            )
        }

        composable(Rutas.PRUEBA, arguments = argUnidad) { entry ->
            val args = entry.arguments!!
            val usuarioId = args.getInt("usuarioId")
            val asignatura = Asignatura.valueOf(args.getString("asignatura")!!)
            val unidad = args.getString("unidad")!!
            PruebaScreen(
                db = db,
                usuarioId = usuarioId,
                asignatura = asignatura,
                unidad = unidad,
                onBack = { navController.popBackStack() },
                onTerminada = { intento ->
                    navController.navigate(Rutas.resultado(usuarioId, asignatura, unidad, intento)) {
                        popUpTo(Rutas.PRUEBA) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.RESULTADO, arguments = argResultado) { entry ->
            val args = entry.arguments!!
            ResultadoScreen(
                db = db,
                usuarioId = args.getInt("usuarioId"),
                asignatura = Asignatura.valueOf(args.getString("asignatura")!!),
                unidad = args.getString("unidad")!!,
                intento = args.getInt("intento"),
                onVolver = { navController.popBackStack() }
            )
        }

        // ---------- DOCENTE Y UTP (provisorias) ----------
        composable(Rutas.DOCENTE, arguments = argUsuario) { entry ->
            DocenteHomeScreen(
                db = db,
                usuarioId = entry.arguments!!.getInt("usuarioId"),
                onLogout = { cerrarSesion() },
                onFormulario = { tipo -> navController.navigate(Rutas.formulario(tipo)) }
            )
        }
        composable(Rutas.UTP, arguments = argUsuario) { entry ->
            UtpHomeScreen(
                db = db,
                usuarioId = entry.arguments!!.getInt("usuarioId"),
                onLogout = { cerrarSesion() },
                onFormulario = { tipo -> navController.navigate(Rutas.formulario(tipo)) }
            )
        }
    }
}