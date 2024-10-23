package no.usn.mob3000.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun ChessbuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = DarkPrimary,
            secondary = DarkSecondary,
            background = DarkBackground,
            surface = DarkSurface,
            onPrimary = DarkOnPrimary,
            onSecondary = LightOnSecondary,
            onBackground = DarkOnBackground,
            onSurface = DarkOnSurface
        )
    } else {
        lightColorScheme(
            primary = LightPrimary,
            secondary = LightSecondary,
            background = LightBackground,
            surface = LightSurface,
            onPrimary = LightOnPrimary,
            onSecondary = DarkOnSecondary,
            onBackground = LightOnBackground,
            onSurface = LightOnSurface
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
