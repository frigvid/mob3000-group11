package no.usn.mob3000.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkDefaultButton,
    secondary = DarkNavbarButtonSelected,
    background = DarkDefaultBackground,
    surface = DarkNavbarBackground,
    onPrimary = DarkProfileUserBackground,
    onSecondary = DarkProfileUserStatisticsBackground,
    onBackground = DarkProfileUserBackground,
    onSurface = DarkProfileUserStatisticsBackground
)

private val LightColorScheme = lightColorScheme(
    primary = DefaultButton,
    secondary = NavbarButtonSelected,
    background = DefaultBackground,
    surface = NavbarBackground,
    onPrimary = DefaultBackground,
    onSecondary = DefaultBackground,
    onBackground = OnDefaultBackground,
    onSurface = ProfileUserStatisticsBackground
)

@Composable
fun ChessbuddyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // TODO: investigate better solution for dynamically switching color schemes
    val colorScheme =
        if (!darkTheme) {
            LightColorScheme
        } else {
            DarkColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
