package cl.duoc.mpazpro.ui.docente

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.ui.components.AppScaffold
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.filled.Star

private const val TAB_OBJETIVOS = "objetivos"
private const val TAB_INFORMES = "informes"

private const val TAB_DESAFIOS = "desafios"

private fun irATab(nav: NavHostController, ruta: String) {
    nav.navigate(ruta) {
        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun DocenteHomeScreen(
    db: AppDatabase,
    usuarioId: Int,
    onLogout: () -> Unit,
    onFormulario: (String) -> Unit = {}
) {
    val tabsNav = rememberNavController()
    val entradaActual by tabsNav.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    AppScaffold(
        titulo = "Panel docente",
        onLogout = onLogout,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = rutaActual == TAB_OBJETIVOS,
                    onClick = { irATab(tabsNav, TAB_OBJETIVOS) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    label = { Text("Objetivos") }
                )
                NavigationBarItem(
                    selected = rutaActual == TAB_INFORMES,
                    onClick = { irATab(tabsNav, TAB_INFORMES) },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                    label = { Text("Informes") }
                )
                NavigationBarItem(
                    selected = rutaActual == TAB_DESAFIOS,
                    onClick = { irATab(tabsNav, TAB_DESAFIOS) },
                    icon = { Icon(Icons.Default.Star, contentDescription = null) },
                    label = { Text("Desafíos") }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = tabsNav,
            startDestination = TAB_OBJETIVOS,
            modifier = Modifier.padding(padding),
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            composable(TAB_OBJETIVOS) { DocenteObjetivosScreen(db, usuarioId, onFormulario) }
            composable(TAB_INFORMES) { DocenteInformesScreen(db, usuarioId) }
            composable(TAB_DESAFIOS) { DocenteDesafiosScreen(db, usuarioId) }
        }
    }
}