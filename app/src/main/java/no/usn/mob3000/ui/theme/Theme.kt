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
    onSecondary = DarkDefaultListItemBackground,
    onBackground = DarkProfileUserBackground,
    onSurface = DarkProfileUserStatisticsBackground,
    error = DarkErrorColor,
    tertiary = DarkProfileBanner,
    onTertiary = DarkOnProfileBanner,
    scrim = DarkProfileScoreboard
)

private val LightColorScheme = lightColorScheme(
    primary = DefaultButton,
    secondary = NavbarButtonSelected,
    background = DefaultBackground,
    surface = NavbarBackground,
    onPrimary = DefaultBackground,
    onSecondary = DefaultListItemBackground,
    onBackground = ProfileUserBackground,
    onSurface = ProfileUserStatisticsBackground,
    error = ErrorColor,
    tertiary = ProfileBanner,
    onTertiary = OnProfileBanner,
    scrim = ProfileScoreboard

)

/**
 * Function that provides a customizable theme for the app, allowing switching between light and dark modes.
 *
 * @param darkTheme Boolean flag indicating if dark theme should be used.
 * @param content Composable content that should be wrapped with the theme.
 * @author markusingierd
 * @created 2024-10-12
 */
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
