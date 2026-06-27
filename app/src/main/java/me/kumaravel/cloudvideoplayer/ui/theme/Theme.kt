package me.kumaravel.cloudvideoplayer.ui.theme

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorSchemeTV = androidx.tv.material3.darkColorScheme(
    primary = Color(0xFFE50914), // Netflix Red
    onPrimary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)

private val DarkColorSchemeMobile = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFFE50914), // Netflix Red
    onPrimary = Color.White,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White
)

@Composable
fun CloudVideoPlayerTheme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isTv = remember { context.packageManager.hasSystemFeature(PackageManager.FEATURE_LEANBACK) }

    if (isTv) {
        androidx.tv.material3.MaterialTheme(
            colorScheme = DarkColorSchemeTV,
            content = content
        )
    } else {
        androidx.compose.material3.MaterialTheme(
            colorScheme = DarkColorSchemeMobile,
            content = content
        )
    }
}
