package cl.duoc.mpazpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import cl.duoc.mpazpro.data.local.AppDatabase
import cl.duoc.mpazpro.data.local.sembrarDatosSiVacio
import cl.duoc.mpazpro.ui.navigation.AppNavHost
import cl.duoc.mpazpro.ui.theme.ProyectoMpazproTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val db = AppDatabase.get(this)
        lifecycleScope.launch { sembrarDatosSiVacio(db) }
        setContent {
            ProyectoMpazproTheme {

                    AppNavHost(db)
                }
            }
        }
    }



