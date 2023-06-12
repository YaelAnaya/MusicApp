package ic.yao.musicapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFF5F5F5),
    primaryContainer = Color(0xFF18191A),
    onPrimaryContainer = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF1A1A1A),
    tertiary = Color(0xFFF3F3F3),
    secondaryContainer = Color(0xFF1A1A1A),
    onSecondaryContainer = Color(0xFFFFFFFF),
    background = Color(0xFF141518),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1A1A1A),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF141518),
    primaryContainer = Color(0xFF18191A),
    onPrimaryContainer = Color(0xFF1B1B1B),
    onPrimary = Color(0xFF222222),
    secondary = Color(0xFFE7E7E7),
    tertiary = Color(0xFFF3F3F3),
    secondaryContainer = Color(0xFF141518),
    background = Color(0xFFF8F8F8),
    onBackground = Color(0xFF181818),
)

@Composable
fun MusicAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}