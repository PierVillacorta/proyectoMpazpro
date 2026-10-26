package cl.duoc.mpazpro.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = Azul800,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulOscuro,
    secondary = Color(0xFF546E8A),
    onSecondary = Color.White,
    secondaryContainer = AzulClaro,
    onSecondaryContainer = AzulOscuro,
    tertiary = Verde,
    background = FondoClaro,
    onBackground = TextoClaro,
    surface = SuperficieClara,
    onSurface = TextoClaro,
    surfaceVariant = TarjetaClara,
    onSurfaceVariant = TextoSecundarioClaro,
    surfaceContainerHighest = Color.White)

private val DarkColorScheme = darkColorScheme(
    primary = Azul200,
    onPrimary = AzulOscuro,
    primaryContainer = AzulProfundo,
    onPrimaryContainer = AzulClaro,
    secondary = Color(0xFFB0C4DE),
    onSecondary = AzulOscuro,
    secondaryContainer = AzulProfundo,
    onSecondaryContainer = AzulClaro,
    tertiary = Color(0xFF81C784),
    background = FondoOscuro,
    onBackground = TextoOscuro,
    surface = SuperficieOscura,
    onSurface = TextoOscuro,
    surfaceVariant = TarjetaOscura,
    onSurfaceVariant = TextoSecundarioOscuro,
    surfaceContainerHighest = TarjetaOscura
)

// Esquinas más redondeadas: más amigables para estudiantes de 6.º
private val AppShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun ProyectoMpazproTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Apagado: con true, Android 12+ ignora nuestro azul y usa los colores del fondo de pantalla
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}