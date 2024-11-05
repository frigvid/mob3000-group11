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
            primary = DarkDefaultButton,
            secondary = DarkNavbarButtonSelected,
            background = DarkDefaultBackground,
            surface = DarkNavbarBackground,
            onPrimary = DarkProfileUserBackground,
            onSecondary = DarkProfileUserStatisticsBackground,
            onBackground = DarkProfileUserBackground,
            onSurface = DarkProfileUserStatisticsBackground
        )
    } else {
        lightColorScheme(
            primary = DefaultButton,
            secondary = NavbarButtonSelected,
            background = DefaultBackground,
            surface = NavbarBackground,
            onPrimary = DefaultBackground,
            onSecondary = DefaultBackground,
            onBackground = OnDefaultBackground,
            onSurface = ProfileUserStatisticsBackground
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}


